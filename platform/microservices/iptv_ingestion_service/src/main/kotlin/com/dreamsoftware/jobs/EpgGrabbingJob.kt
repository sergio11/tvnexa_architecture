package com.dreamsoftware.jobs

import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.iptvorg.model.EpgDTO
import com.dreamsoftware.jobs.core.*
import com.dreamsoftware.model.EpgGrabbingConfig
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.quartz.*
import java.io.File
import java.util.*

@DisallowConcurrentExecution
class EpgGrabbingJob(
    private val channelGuideDatabaseDataSource: IChannelGuideDatabaseDataSource,
    private val kotlinXmlMapper: ObjectMapper,
    private val epgGrabbingConfig: EpgGrabbingConfig
) : SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val languageId = jobData?.getString(LANGUAGE_ID_ARG).orEmpty()
        if (languageId.isEmpty())
            throw IllegalStateException("You must provide language id")

        channelGuideDatabaseDataSource.findByLanguageId(languageId).map {
            async { Pair(it.site, runEpgGrabberAsync(it.site)) }
        }.awaitAll().filter { it.second != EPG_GRABBER_FAILED }.let { grabberResults ->
            with(grabberResults) {
                if (isEmpty()) {
                    log.debug("EPG Grabber FAILED for language $languageId")
                    throw RuntimeException("EPG Grabber FAILED for language $languageId")
                } else {
                    map {
                        with(epgGrabbingConfig) {
                            sitesBaseFolder + outputGuidesPath.replace("{lang}", languageId)
                                .replace("{site}", it.first)
                        }
                    }
                }
            }
        }.map { guidePath ->
            kotlinXmlMapper.readValue(File(guidePath), EpgDTO::class.java)
        }.let {
            log.debug("EPG results size -> ${it.size}")
        }
    }

    private suspend fun runEpgGrabberAsync(site: String) = withContext(Dispatchers.IO) {
        with(epgGrabbingConfig) {
            ProcessBuilder(
                npmCmd,
                "epg-grabber",
                "--config=${jsConfigPath.replace("{site}", site)}",
                "--channels=${channelsPath.replace("{site}", site)}",
                "--output=$outputGuidesPath"
            )
                .directory(File(sitesBaseFolder))
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()
        }
    }


    private fun isWindows(): Boolean =
        System.getProperty("os.name")
            .lowercase(Locale.getDefault())
            .contains("win")

    private val npmCmd = if (isWindows()) "npx.cmd" else "npx"

    companion object : IJobBuilder {

        const val LANGUAGE_ID_ARG = "LANGUAGE_ID_ARG"

        private const val EPG_GRABBER_FAILED = 1
        private const val DEFAULT_JOB_ID = "epg_grabbing_job"
        private const val DEFAULT_TRIGGER_ID = "epg_grabbing_job_trigger"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<EpgGrabbingJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun buildTrigger(triggerId: String?): Trigger = createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}