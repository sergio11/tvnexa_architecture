package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger

class LanguagesIngestionJob(
    private val languageNetworkDataSource: IptvOrgNetworkDataSource<LanguageDTO>,
    private val languageMapper: IMapper<LanguageDTO, SaveLanguageEntity>,
    private val languageDatabaseDataSource: ILanguageDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val languages = languageNetworkDataSource.fetchContent()
        log.debug("${languages.count()} languages will be processed")
        languageDatabaseDataSource.save(languageMapper.mapList(languages))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "languages_ingestion_job"
        private const val TRIGGER_ID = "ingest_languages_job_trigger"
        private const val INTERVAL_IN_MINUTES = 10

        override fun buildJob(): JobDetail = createNewJob<LanguagesIngestionJob>(JOB_ID)
        override fun buildTrigger(): Trigger = createNewTrigger(TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}