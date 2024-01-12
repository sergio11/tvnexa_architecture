package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*

/**
 * LanguagesIngestionJob is a Quartz Job that fetches and ingests language data from an external source
 * into the database.
 *
 * @param languageNetworkDataSource The data source for fetching language data.
 * @param languageMapper Mapper for converting LanguageDTO to SaveLanguageEntity.
 * @param languageDatabaseDataSource The database data source for storing language data.
 */
@DisallowConcurrentExecution
class LanguagesIngestionJob(
    private val languageNetworkDataSource: IptvOrgNetworkDataSource<LanguageDTO>,
    private val languageMapper: ISimpleMapper<LanguageDTO, SaveLanguageEntity>,
    private val languageDatabaseDataSource: ILanguageDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting LanguagesIngestionJob execution.")

        try {
            // Fetch language data from an external source
            val languages = languageNetworkDataSource.fetchContent()

            // Log the number of languages to be processed
            log.debug("${languages.count()} languages will be processed")

            // Save the fetched language data to the database
            languageDatabaseDataSource.save(languageMapper.mapList(languages))

            log.debug("LanguagesIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during LanguagesIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "languages_ingestion_job"
        private const val DEFAULT_TRIGGER_ID = "ingest_languages_job_trigger"
        private const val INTERVAL_IN_MINUTES = 10

        /**
         * Build a JobDetail instance for the LanguagesIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the LanguagesIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<LanguagesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Build a Trigger instance for the LanguagesIngestionJob.
         *
         * @param triggerId The optional custom trigger ID.
         * @return A Trigger instance for the LanguagesIngestionJob.
         */
        override fun buildTrigger(triggerId: String?): Trigger =
            createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)

        /**
         * Get the JobKey for the LanguagesIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the LanguagesIngestionJob.
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