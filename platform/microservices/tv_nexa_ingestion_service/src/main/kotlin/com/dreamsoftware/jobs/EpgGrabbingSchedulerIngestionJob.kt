package com.dreamsoftware.jobs

import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.jobs.EpgGrabbingJob.Companion.LANGUAGE_ID_ARG
import com.dreamsoftware.jobs.EpgGrabbingJob.Companion.SITE_ID_ARG
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

@DisallowConcurrentExecution
class EpgGrabbingSchedulerIngestionJob(
    private val guidesDatabaseDataSource: IChannelGuideDatabaseDataSource
) : SupportJob() {

    // This method is called when the job starts its execution
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting EpgGrabbingSchedulesIngestionJob execution.")

        scheduler?.let {
            with(scheduler) {
                val channelGuides = guidesDatabaseDataSource.findGroupBySiteAndLanguage()
                channelGuides.forEach { guideGroup ->
                    log.debug("Schedule Epg Grabbing Job for Guide group: site: ${guideGroup.site}, lang: ${guideGroup.lang}, channel count: ${guideGroup.count}")
                    val epgKey = EpgGrabbingJob.buildKey(languageId = guideGroup.lang, site = guideGroup.site)
                    if (!checkExists(EpgGrabbingJob.getJobKey(epgKey))) {
                        // Build and schedule an EpgGrabbingJob for the language
                        scheduleJob(
                            EpgGrabbingJob.buildJob(
                                jobId = epgKey,
                                data = mapOf(
                                    LANGUAGE_ID_ARG to guideGroup.lang,
                                    SITE_ID_ARG to guideGroup.site
                                )
                            ).also {
                                log.debug("JobDataMap For Job $epgKey")
                                it.jobDataMap.forEach { item ->
                                    log.debug("JobDataMap key: ${item.key}, value: ${item.value}")
                                }
                            },
                            EpgGrabbingJob.buildTrigger(
                                EpgGrabbingJob.buildTriggerKey(languageId = guideGroup.lang, site = guideGroup.site)
                            )
                        )
                        log.debug("Scheduled job $epgKey completed!")
                    }
                }
            }
        }
        log.debug("EpgGrabbingSchedulesIngestionJob execution completed.")
    }

    companion object : IJobBuilder {

        private const val DEFAULT_JOB_ID = "epg_grabbing_scheduler_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 5

        // Implement the methods from the IJobBuilder interface
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<EpgGrabbingSchedulerIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelGuidesIngestionJob.getJobKey()
    }
}