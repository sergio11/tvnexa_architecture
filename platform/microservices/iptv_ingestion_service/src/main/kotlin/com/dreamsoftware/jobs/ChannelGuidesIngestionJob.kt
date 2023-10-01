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

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val guides = guidesNetworkDataSource.fetchContent()
        val savedGuides = guidesDatabaseDataSource.findAll()
        val guideSites = guides.map { it.site }
        scheduler?.let {
            with(scheduler) {
                savedGuides.filterNot { guideSites.contains(it.site) }.forEach { deletedGuide ->
                    val jobId = EPG_GRABBING_JOB_ID.replace("{lang}", deletedGuide.lang)
                    deleteJob(JobKey.jobKey(jobId))
                }
                guides.groupBy { it.lang }.forEach { group ->
                    if (!checkExists(EpgGrabbingJob.getJobKey(EPG_GRABBING_JOB_ID.replace("{lang}", group.key)))) {
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
                    }
                }
            }
        }
        guidesDatabaseDataSource.save(guidesMapper.mapList(guides))
    }

    companion object : IJobBuilder {

        private const val EPG_GRABBING_JOB_ID = "epg_grabbing_{lang}_job"
        private const val EPG_GRABBING_TRIGGER_ID = "epg_grabbing_{lang}_trigger"
        private const val DEFAULT_JOB_ID = "channel_guides_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 5


        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<ChannelGuidesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}