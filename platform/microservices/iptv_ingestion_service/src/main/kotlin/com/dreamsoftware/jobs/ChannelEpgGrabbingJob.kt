package com.dreamsoftware.jobs

import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.jobs.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.quartz.*
import java.io.File
import java.util.*

@DisallowConcurrentExecution
class ChannelEpgGrabbingJob(
    private val channelGuideDatabaseDataSource: IChannelGuideDatabaseDataSource
) : SupportJob() {

    private val npmCmd = if (isWindows()) "npx.cmd" else "npx"
    private val baseFolder = "C:\\Users\\ssanchez\\Downloads\\epg-master\\epg-master"

    override suspend fun onStartExecution(jobData: JobDataMap?) {
        val channelId = jobData?.getString(CHANNEL_ID_ARG).orEmpty()
        if (channelId.isEmpty())
            throw IllegalStateException("You must provide channel id")

        val channelGuides = channelGuideDatabaseDataSource.findByChannelId(channelId)
        val results = channelGuides.map {
            runEpgGrabberAsync(it.site)
        }.awaitAll()

        if (results.contains(1)) {
            log.debug("EPG Grabber for channel $channelId failed!")
            throw RuntimeException("EPG Grabber for channel $channelId failed!")
        }
    }

    private fun runEpgGrabberAsync(site: String) = async(Dispatchers.IO) {
        ProcessBuilder(
            npmCmd,
            "epg-grabber",
            "--config=sites\\$site\\$site.config.js",
            "--channels=sites\\$site\\$site.channels.xml",
            "--output=guides\\{lang}\\{site}.xml"
        )
            .directory(File(baseFolder))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor()
    }


    private fun isWindows(): Boolean =
        System.getProperty("os.name")
            .lowercase(Locale.getDefault())
            .contains("win")

    companion object : IJobBuilder {

        const val CHANNEL_ID_ARG = "CHANNEL_ID_ARG"

        private const val JOB_ID = "channel_epg_grabbing_job"
        private const val TRIGGER_ID = "channel_epg_grabbing_job_trigger"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(): JobDetail = createNewJob<ChannelEpgGrabbingJob>(JOB_ID)
        override fun buildTrigger(): Trigger = createNewTrigger(TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}