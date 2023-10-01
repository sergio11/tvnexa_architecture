package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity
import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.data.epg.model.EpgDataDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*

@DisallowConcurrentExecution
class EpgGrabbingJob(
    private val channelGuideDatabaseDataSource: IChannelGuideDatabaseDataSource,
    private val epgChannelProgrammeDatabaseDataSource: IEpgChannelProgrammeDatabaseDataSource,
    private val epgGrabbingDataSource: IEpgGrabbingDataSource,
    private val epgDataMapper: ISimpleMapper<EpgDataDTO, SaveEpgChannelProgrammeEntity>
) : SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val languageId = jobData?.getString(LANGUAGE_ID_ARG).orEmpty()
        if (languageId.isEmpty())
            throw IllegalStateException("You must provide language id")
        epgGrabbingDataSource.fetchEpgForSites(
            languageId,
            channelGuideDatabaseDataSource.findByLanguageId(languageId).map { it.site })
            .let(epgDataMapper::mapList)
            .let {
                epgChannelProgrammeDatabaseDataSource.save(it)
            }
    }


    companion object : IJobBuilder {

        const val LANGUAGE_ID_ARG = "LANGUAGE_ID_ARG"

        private const val DEFAULT_JOB_ID = "epg_grabbing_job"
        private const val DEFAULT_TRIGGER_ID = "epg_grabbing_job_trigger"
        private const val INTERVAL_IN_MINUTES = 5

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<EpgGrabbingJob>(jobId ?: DEFAULT_JOB_ID, data)

        override fun buildTrigger(triggerId: String?): Trigger =
            createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)

        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}