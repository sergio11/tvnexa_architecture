package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.IJobBuilder.Companion.JOB_MAP_NAME_ID_KEY
import com.dreamsoftware.jobs.core.IJobBuilder.Companion.WATCH_JOB_GROUP
import com.dreamsoftware.jobs.core.SupportJob
import org.quartz.*

class IngestLanguagesJob(
    private val languageNetworkDataSource: IptvOrgNetworkDataSource<LanguageDTO>,
    private val languageMapper: IMapper<LanguageDTO, SaveLanguageEntity>,
    private val languageDatabaseDataSource: ILanguageDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val languages = languageNetworkDataSource.fetchContent()
        languageDatabaseDataSource.save(languageMapper.mapList(languages))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_languages_job"
        private const val TRIGGER_ID = "ingest_languages_job_trigger"

        override fun buildJob(): JobDetail = JobBuilder.newJob(IngestLanguagesJob::class.java)
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