package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveRegionEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.RegionDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

/**
 * RegionsIngestionJob is a Quartz Job that fetches and ingests region data from an external source
 * into the database.
 *
 * @param regionsNetworkDataSource The data source for fetching region data.
 * @param regionMapper Mapper for converting RegionDTO to SaveRegionEntity.
 * @param regionsDatabaseDataSource The database data source for storing region data.
 */
@DisallowConcurrentExecution
class RegionsIngestionJob(
    private val regionsNetworkDataSource: IptvOrgNetworkDataSource<RegionDTO>,
    private val regionMapper: ISimpleMapper<RegionDTO, SaveRegionEntity>,
    private val regionsDatabaseDataSource: IRegionDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting RegionsIngestionJob execution.")

        try {
            // Fetch region data from an external source
            val regions = regionsNetworkDataSource.fetchContent()

            // Log the number of regions to be processed
            log.debug("${regions.count()} regions will be processed")

            // Save the fetched region data to the database
            regionsDatabaseDataSource.save(regionMapper.mapList(regions))

            log.debug("RegionsIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during RegionsIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "regions_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 1

        /**
         * Build a JobDetail instance for the RegionsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the RegionsIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<RegionsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Get the JobKey for the RegionsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the RegionsIngestionJob.
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