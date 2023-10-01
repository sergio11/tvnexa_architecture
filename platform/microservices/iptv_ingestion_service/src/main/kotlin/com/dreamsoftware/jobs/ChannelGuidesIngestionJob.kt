package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelGuideDTO
import com.dreamsoftware.jobs.EpgGrabbingJob.Companion.LANGUAGE_ID_ARG
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

@DisallowConcurrentExecution
class ChannelGuidesIngestionJob(
    private val guidesNetworkDataSource: IptvOrgNetworkDataSource<ChannelGuideDTO>,
    private val guidesMapper: ISimpleMapper<ChannelGuideDTO, SaveChannelGuideEntity>,
    private val guidesDatabaseDataSource: IChannelGuideDatabaseDataSource
) : SupportJob() {

    // This method is called when the job starts its execution
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting ChannelGuidesIngestionJob execution.")
        // Fetch channel guides from an external source
        val guides = guidesNetworkDataSource.fetchContent()

        // Fetch saved channel guides from the database
        val savedGuides = guidesDatabaseDataSource.findAll()

        // Extract unique guide sites
        val guideSites = guides.map { it.site }

        scheduler?.let {
            with(scheduler) {
                // Iterate through saved guides and delete jobs for guides that are no longer present
                savedGuides.filterNot { guideSites.contains(it.site) }.forEach { deletedGuide ->
                    val jobId = EPG_GRABBING_JOB_ID.replace("{lang}", deletedGuide.lang)
                    deleteJob(JobKey.jobKey(jobId))
                    log.debug("Deleted job for guide with language ${deletedGuide.lang}.")
                }

                // Group fetched guides by language and schedule jobs for each language
                guides.groupBy { it.lang }.forEach { group ->
                    if (!checkExists(EpgGrabbingJob.getJobKey(EPG_GRABBING_JOB_ID.replace("{lang}", group.key)))) {
                        // Build and schedule an EpgGrabbingJob for the language
                        scheduleJob(
                            EpgGrabbingJob.buildJob(
                                jobId = EPG_GRABBING_JOB_ID.replace("{lang}", group.key),
                                data = buildMap {
                                    LANGUAGE_ID_ARG to group.key
                                }),
                            EpgGrabbingJob.buildTrigger(
                                EPG_GRABBING_TRIGGER_ID.replace("{lang}", group.key)
                            )
                        )
                        log.debug("Scheduled job for guide with language ${group.key}.")
                    }
                }
            }
        }

        // Save the fetched channel guides to the database
        guidesDatabaseDataSource.save(guidesMapper.mapList(guides))
        log.debug("Saved ${guides.count()} channel guides to the database.")
        log.debug("ChannelGuidesIngestionJob execution completed.")
    }

    companion object : IJobBuilder {
        private const val EPG_GRABBING_JOB_ID = "epg_grabbing_{lang}_job"
        private const val EPG_GRABBING_TRIGGER_ID = "epg_grabbing_{lang}_trigger"
        private const val DEFAULT_JOB_ID = "channel_guides_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 5

        // Implement the methods from the IJobBuilder interface
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<ChannelGuidesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}