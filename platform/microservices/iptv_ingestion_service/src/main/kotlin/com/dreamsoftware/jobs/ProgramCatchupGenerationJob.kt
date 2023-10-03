package com.dreamsoftware.jobs

import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.jobs.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.quartz.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Duration

@DisallowConcurrentExecution
class ProgramCatchupGenerationJob(
    private val epgChannelProgrammeDatabaseDataSource: IEpgChannelProgrammeDatabaseDataSource,
) : SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val epgChannelProgrammeId = jobData?.getLong(EPG_CHANNEL_PROGRAMME_ID_ARG)
        val channelStreamUrl = jobData?.getString(CHANNEL_STREAM_URL_ARG).orEmpty()

        if (epgChannelProgrammeId == null) {
            log.error("epgChannelProgrammeId is empty. Cannot generate catchup without epgChannelProgrammeId.")
            throw IllegalStateException("You must provide a epgChannelProgrammeId")
        }

        if (channelStreamUrl.isEmpty()) {
            log.error("channelStreamUrl is empty. Cannot generate catchup without channelStreamUrl.")
            throw IllegalStateException("You must provide a channelStreamUrl")
        }

        log.debug("Starting ProgramCatchupGenerationJob execution for epgChannelProgrammeId : $epgChannelProgrammeId")
        val epgChannelProgramme = epgChannelProgrammeDatabaseDataSource.findByKey(epgChannelProgrammeId)
        val durationInSeconds = Duration.between(epgChannelProgramme.start, epgChannelProgramme.end).seconds
        log.debug("Duration of the program (seconds): $durationInSeconds")

        val ffmpegCommand = """
            ffmpeg -i $channelStreamUrl
            -c:v libx264 -preset fast -b:v 1000k 
            -c:a aac -b:a 192k                 
            -t $durationInSeconds
            -vf "scale=1280:720"
            output.mp4
        """.trimIndent()

        try {
            withContext(Dispatchers.IO) {
                val process = Runtime.getRuntime().exec(ffmpegCommand)
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    log.debug("FFMPEG Output: $line")
                }
                val exitCode = process.waitFor()
                if (exitCode == 0) {
                    log.debug("FFMPEG process completed successfully.")
                } else {
                    log.error("FFMPEG process exited with code $exitCode")
                }
            }
        } catch (e: Exception) {
            log.error("Error during FFMPEG execution: ${e.message}", e)
        }
        log.debug("ProgramCatchupGenerationJob execution completed successfully.")
    }

    companion object : IJobBuilder {

        const val EPG_CHANNEL_PROGRAMME_ID_ARG = "EPG_CHANNEL_PROGRAMME_ID_ARG"
        const val CHANNEL_STREAM_URL_ARG = "CHANNEL_STREAM_URL_ARG"

        const val DEFAULT_JOB_ID = "program_catchup_generation_job"
        const val DEFAULT_TRIGGER_ID = "program_catchup_generation_job_trigger"
        private const val INTERVAL_IN_MINUTES = 5

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<ProgramCatchupGenerationJob>(jobId ?: DEFAULT_JOB_ID, data)

        override fun buildTrigger(triggerId: String?): Trigger =
            createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)

        override fun getJobKey(jobId: String?): JobKey =
            createJobKey(jobId ?: DEFAULT_JOB_ID)

        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}
