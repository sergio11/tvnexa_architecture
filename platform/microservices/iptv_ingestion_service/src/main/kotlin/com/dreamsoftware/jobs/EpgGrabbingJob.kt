package com.dreamsoftware.jobs

import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.jobs.core.*
import org.quartz.*

@DisallowConcurrentExecution
class EpgGrabbingJob(
    private val channelGuideDatabaseDataSource: IChannelGuideDatabaseDataSource,
    private val epgGrabbingDataSource: IEpgGrabbingDataSource
) : SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val languageId = jobData?.getString(LANGUAGE_ID_ARG).orEmpty()
        if (languageId.isEmpty())
            throw IllegalStateException("You must provide language id")

        channelGuideDatabaseDataSource.findByLanguageId(languageId).let { guidesByLanguage ->
            epgGrabbingDataSource.fetchEpgForSites(languageId, guidesByLanguage.map { it.site })
        }
    }



    companion object : IJobBuilder {

        const val LANGUAGE_ID_ARG = "LANGUAGE_ID_ARG"

        private const val DEFAULT_JOB_ID = "epg_grabbing_job"
        private const val DEFAULT_TRIGGER_ID = "epg_grabbing_job_trigger"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<EpgGrabbingJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun buildTrigger(triggerId: String?): Trigger = createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}