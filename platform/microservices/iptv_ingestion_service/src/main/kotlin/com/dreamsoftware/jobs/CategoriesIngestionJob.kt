package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.CategoryDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*

/**
 * CategoriesIngestionJob is a Quartz Job that fetches and ingests category data from an external source
 * into the database.
 *
 * @param categoriesNetworkDataSource The data source for fetching category data.
 * @param categoriesMapper Mapper for converting CategoryDTO to SaveCategoryEntity.
 * @param categoriesDatabaseDataSource The database data source for storing category data.
 */
@DisallowConcurrentExecution
class CategoriesIngestionJob(
    private val categoriesNetworkDataSource: IptvOrgNetworkDataSource<CategoryDTO>,
    private val categoriesMapper: ISimpleMapper<CategoryDTO, SaveCategoryEntity>,
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting CategoriesIngestionJob execution.")

        try {
            // Fetch category data from an external source
            val categories = categoriesNetworkDataSource.fetchContent()

            // Log the number of categories to be processed
            log.debug("${categories.count()} categories will be processed")

            // Save the fetched category data to the database
            categoriesDatabaseDataSource.save(categoriesMapper.mapList(categories))

            log.debug("CategoriesIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during CategoriesIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job and trigger IDs
        private const val DEFAULT_JOB_ID = "categories_ingestion_job"
        private const val DEFAULT_TRIGGER_ID = "ingest_categories_job_trigger"

        // Default interval for job execution (in minutes)
        private const val INTERVAL_IN_MINUTES = 5

        /**
         * Build a JobDetail instance for the CategoriesIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the CategoriesIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<CategoriesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Build a Trigger instance for the CategoriesIngestionJob.
         *
         * @param triggerId The optional custom trigger ID.
         * @return A Trigger instance for the CategoriesIngestionJob.
         */
        override fun buildTrigger(triggerId: String?): Trigger =
            createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)

        /**
         * Get the JobKey for the CategoriesIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the CategoriesIngestionJob.
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