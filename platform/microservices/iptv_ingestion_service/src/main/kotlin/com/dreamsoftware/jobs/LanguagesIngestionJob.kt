package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*

@DisallowConcurrentExecution
class LanguagesIngestionJob(
    private val languageNetworkDataSource: IptvOrgNetworkDataSource<LanguageDTO>,
    private val languageMapper: ISimpleMapper<LanguageDTO, SaveLanguageEntity>,
    private val languageDatabaseDataSource: ILanguageDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val languages = languageNetworkDataSource.fetchContent()
        log.debug("${languages.count()} languages will be processed")
        languageDatabaseDataSource.save(languageMapper.mapList(languages))
    }

    companion object: IJobBuilder {

        private const val DEFAULT_JOB_ID = "languages_ingestion_job"
        private const val DEFAULT_TRIGGER_ID = "ingest_languages_job_trigger"
        private const val INTERVAL_IN_MINUTES = 10

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<LanguagesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun buildTrigger(triggerId: String?): Trigger = createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}