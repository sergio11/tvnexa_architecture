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

    override suspend fun onStartExecution(jobData: JobDataMap?) {
        val categories = categoriesNetworkDataSource.fetchContent()
        categoriesDatabaseDataSource.save(categoriesMapper.mapList(categories))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "categories_ingestion_job"
        private const val TRIGGER_ID = "ingest_categories_job_trigger"
        private const val INTERVAL_IN_MINUTES = 5

        override fun buildJob(): JobDetail = createNewJob<CategoriesIngestionJob>(JOB_ID)
        override fun buildTrigger(): Trigger = createNewTrigger(TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}