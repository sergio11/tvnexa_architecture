package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.CategoryDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger

class CategoriesIngestionJob(
    private val categoriesNetworkDataSource: IptvOrgNetworkDataSource<CategoryDTO>,
    private val categoriesMapper: IMapper<CategoryDTO, SaveCategoryEntity>,
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val categories = categoriesNetworkDataSource.fetchContent()
        categoriesDatabaseDataSource.save(categoriesMapper.mapList(categories))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_categories_job"
        private const val TRIGGER_ID = "ingest_categories_job_trigger"
        private const val INTERVAL_IN_MINUTES = 2

        override fun buildJob(): JobDetail = createNewJob<CategoriesIngestionJob>(JOB_ID)
        override fun buildTrigger(): Trigger = createNewTrigger(TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
    }
}