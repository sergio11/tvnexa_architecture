package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity
import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.data.epg.model.EpgDataDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*

/**
 * EpgGrabbingJob is a Quartz Job that performs Electronic Program Guide (EPG) data grabbing and ingestion
 * into the database.
 *
 * @param channelGuideDatabaseDataSource The database data source for channel guide information.
 * @param epgChannelProgrammeDatabaseDataSource The database data source for EPG channel programme data.
 * @param epgGrabbingDataSource The data source for fetching EPG data.
 * @param epgDataMapper Mapper for converting EpgDataDTO to SaveEpgChannelProgrammeEntity.
 */
@DisallowConcurrentExecution
class EpgGrabbingJob(
    private val channelGuideDatabaseDataSource: IChannelGuideDatabaseDataSource,
    private val epgChannelProgrammeDatabaseDataSource: IEpgChannelProgrammeDatabaseDataSource,
    private val epgGrabbingDataSource: IEpgGrabbingDataSource,
    private val epgDataMapper: ISimpleMapper<EpgDataDTO, SaveEpgChannelProgrammeEntity>
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val languageId = jobData?.getString(LANGUAGE_ID_ARG).orEmpty()

        log.debug("Starting EpgGrabbingJob execution for language ID: $languageId")

        if (languageId.isEmpty()) {
            log.error("Language ID is empty. Cannot fetch EPG data without language ID.")
            throw IllegalStateException("You must provide a language ID")
        }

        // Fetch EPG data for specified language and channel sites
        val channelSites = channelGuideDatabaseDataSource.findByLanguageId(languageId).map { it.site }
        val epgData = epgGrabbingDataSource.fetchEpgForSites(languageId, channelSites)

        // Map and save the fetched EPG data to the database
        val mappedEpgData = epgDataMapper.mapList(epgData)
        epgChannelProgrammeDatabaseDataSource.save(mappedEpgData)

        log.debug("EpgGrabbingJob execution completed successfully.")
    }

    companion object : IJobBuilder {

        const val LANGUAGE_ID_ARG = "LANGUAGE_ID_ARG"

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "epg_grabbing_job"
        private const val DEFAULT_TRIGGER_ID = "epg_grabbing_job_trigger"
        private const val INTERVAL_IN_MINUTES = 5

        /**
         * Build a JobDetail instance for the EpgGrabbingJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the EpgGrabbingJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<EpgGrabbingJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Build a Trigger instance for the EpgGrabbingJob.
         *
         * @param triggerId The optional custom trigger ID.
         * @return A Trigger instance for the EpgGrabbingJob.
         */
        override fun buildTrigger(triggerId: String?): Trigger =
            createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)

        /**
         * Get the JobKey for the EpgGrabbingJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the EpgGrabbingJob.
         */
        override fun getJobKey(jobId: String?): JobKey =
            createJobKey(jobId ?: DEFAULT_JOB_ID)

        /**
         * Get the interval (in minutes) at which the job should be executed.
         *
         * @return The execution interval in minutes.
         */
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}