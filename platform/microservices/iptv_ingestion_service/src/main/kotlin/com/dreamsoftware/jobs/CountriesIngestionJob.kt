package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.CountryDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

/**
 * CountriesIngestionJob is a Quartz Job that fetches and ingests country data from an external source
 * into the database.
 *
 * @param countriesNetworkDataSource The data source for fetching country data.
 * @param countriesMapper Mapper for converting CountryDTO to SaveCountryEntity.
 * @param countriesDatabaseDataSource The database data source for storing country data.
 */
@DisallowConcurrentExecution
class CountriesIngestionJob(
    private val countriesNetworkDataSource: IptvOrgNetworkDataSource<CountryDTO>,
    private val countriesMapper: ISimpleMapper<CountryDTO, SaveCountryEntity>,
    private val countriesDatabaseDataSource: ICountryDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting CountriesIngestionJob execution.")

        try {
            // Fetch country data from an external source
            val countries = countriesNetworkDataSource.fetchContent()

            // Log the number of countries to be processed
            log.debug("${countries.count()} countries will be processed")

            // Save the fetched country data to the database
            countriesDatabaseDataSource.save(countriesMapper.mapList(countries))

            log.debug("CountriesIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during CountriesIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "countries_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 1

        /**
         * Build a JobDetail instance for the CountriesIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the CountriesIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<CountriesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Get the JobKey for the CountriesIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the CountriesIngestionJob.
         */
        override fun getJobKey(jobId: String?): JobKey =
            createJobKey(jobId ?: DEFAULT_JOB_ID)

        /**
         * Get the interval (in minutes) at which the job should be executed.
         *
         * @return The execution interval in minutes.
         */
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES

        /**
         * Get the parent job key that this job depends on.
         *
         * @return The JobKey of the parent job.
         */
        override fun getParentJobKey(): JobKey = LanguagesIngestionJob.getJobKey()
    }
}