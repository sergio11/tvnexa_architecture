package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.SubdivisionDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

/**
 * SubdivisionsIngestionJob is a Quartz Job that fetches and ingests subdivision data from an external source
 * into the database.
 *
 * @param subdivisionsNetworkDataSource The data source for fetching subdivision data.
 * @param subdivisionsMapper Mapper for converting SubdivisionDTO to SaveSubdivisionEntity.
 * @param subdivisionsDatabaseDataSource The database data source for storing subdivision data.
 */
@DisallowConcurrentExecution
class SubdivisionsIngestionJob(
    private val subdivisionsNetworkDataSource: IptvOrgNetworkDataSource<SubdivisionDTO>,
    private val subdivisionsMapper: ISimpleMapper<SubdivisionDTO, SaveSubdivisionEntity>,
    private val subdivisionsDatabaseDataSource: ISubdivisionDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting SubdivisionsIngestionJob execution.")

        try {
            // Fetch subdivision data from an external source
            val subdivisions = subdivisionsNetworkDataSource.fetchContent()

            // Log the number of subdivisions to be processed
            log.debug("${subdivisions.count()} subdivisions will be processed")

            // Save the fetched subdivision data to the database
            subdivisionsDatabaseDataSource.save(subdivisionsMapper.mapList(subdivisions))

            log.debug("SubdivisionsIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during SubdivisionsIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "subdivisions_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 1

        /**
         * Build a JobDetail instance for the SubdivisionsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the SubdivisionsIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<SubdivisionsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Get the JobKey for the SubdivisionsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the SubdivisionsIngestionJob.
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
        override fun getParentJobKey(): JobKey = CountriesIngestionJob.getJobKey()
    }
}