package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.CategoryDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.IJobBuilder.Companion.JOB_MAP_NAME_ID_KEY
import com.dreamsoftware.jobs.core.IJobBuilder.Companion.WATCH_JOB_GROUP
import com.dreamsoftware.jobs.core.SupportJob
import org.quartz.*

class IngestCategoriesJob(
    private val categoriesNetworkDataSource: IptvOrgNetworkDataSource<CategoryDTO>,
    private val categoriesMapper: IMapper<CategoryDTO, SaveCategoryEntity>,
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {

    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_languages_job"
        private const val TRIGGER_ID = "ingest_languages_job_trigger"

        override fun buildJob(): JobDetail = JobBuilder.newJob(IngestCategoriesJob::class.java)
            .withIdentity(JOB_ID, WATCH_JOB_GROUP)
            .usingJobData(JOB_MAP_NAME_ID_KEY, JOB_ID)
            .build()

        override fun buildTrigger(): Trigger = TriggerBuilder.newTrigger()
            .withIdentity(TRIGGER_ID, WATCH_JOB_GROUP)
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    // every minute
                    .withIntervalInMinutes(1)
                    .repeatForever()
            )
            .build()

        override fun getJobKey(): JobKey = JobKey.jobKey(JOB_ID, WATCH_JOB_GROUP)

    }
}