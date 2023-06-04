package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.CategoryDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*

@DisallowConcurrentExecution
class CategoriesIngestionJob(
    private val categoriesNetworkDataSource: IptvOrgNetworkDataSource<CategoryDTO>,
    private val categoriesMapper: IMapper<CategoryDTO, SaveCategoryEntity>,
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val categories = categoriesNetworkDataSource.fetchContent()
        categoriesDatabaseDataSource.save(categoriesMapper.mapList(categories))
    }

    companion object: IJobBuilder {

        private const val DEFAULT_JOB_ID = "categories_ingestion_job"
        private const val DEFAULT_TRIGGER_ID = "ingest_categories_job_trigger"
        private const val INTERVAL_IN_MINUTES = 5

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<CategoriesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun buildTrigger(triggerId: String?): Trigger = createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}