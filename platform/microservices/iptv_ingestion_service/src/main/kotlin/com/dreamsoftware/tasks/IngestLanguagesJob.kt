package com.dreamsoftware.tasks

import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.iptvorg.datasource.language.ILanguageNetworkDataSource
import com.dreamsoftware.tasks.core.IJobBuilder
import com.dreamsoftware.tasks.core.IJobBuilder.Companion.JOB_MAP_NAME_ID_KEY
import com.dreamsoftware.tasks.core.IJobBuilder.Companion.WATCH_JOB_GROUP
import org.quartz.*
import org.slf4j.LoggerFactory

class IngestLanguagesJob(
    private val languageNetworkDataSource: ILanguageNetworkDataSource,
    private val languageDatabaseDataSource: ILanguageDatabaseDataSource
): Job {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun execute(context: JobExecutionContext?) {
        log.debug("IngestLanguagesJob execute")

        log.debug("IngestLanguagesJob finish")
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_languages_job"
        private const val TRIGGER_ID = "ingest_languages_job_trigger"

        override fun buildJob(): JobDetail = JobBuilder.newJob(IngestLanguagesJob::class.java)
            .withIdentity(JOB_ID, WATCH_JOB_GROUP)
            .usingJobData(JOB_MAP_NAME_ID_KEY, "test")
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