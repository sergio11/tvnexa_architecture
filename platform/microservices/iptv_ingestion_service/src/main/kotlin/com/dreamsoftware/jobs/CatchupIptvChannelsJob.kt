package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.iptvorg.model.ChannelDTO
import com.dreamsoftware.jobs.core.*
import org.quartz.*
import java.time.LocalDateTime

/**
 * CatchupIptvChannelsJob is a Quartz Job that fetches and processes catchup IPTV channels.
 *
 * @param channelDataSource The data source for fetching channel data.
 * @param channelMapper Mapper for converting ChannelDTO to SaveChannelEntity.
 * @param epgChannelProgrammeDataSource
 */
@DisallowConcurrentExecution
class CatchupIptvChannelsJob(
    private val channelDataSource: IChannelDatabaseDataSource,
    private val channelMapper: ISimpleMapper<ChannelDTO, SaveChannelEntity>,
    private val epgChannelProgrammeDataSource: IEpgChannelProgrammeDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting CatchupIptvChannelsJob execution.")
        try {
            // Fetch catchup IPTV channels
            val catchupChannels = channelDataSource.findChannelsAllowingCatchup()
            log.debug("${catchupChannels.count()} catchup IPTV channels will be processed")
            // Process each catchup channel
            for (channel in catchupChannels) {
                // Check if the stream is not null and has a valid URL
                channel.stream?.url.takeIf { !it.isNullOrBlank() }?.let { streamUrl ->
                    // Process the channel with valid IPTV stream URL
                    if(scheduler != null) {
                        processIptvChannel(scheduler, channel.channelId, streamUrl)
                    }
                } ?: run {
                    log.debug("${channel.name} has not a valid IPTV stream URL")
                }
            }
            log.debug("CatchupIptvChannelsJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during CatchupIptvChannelsJob execution: ${e.message}", e)
        }
    }

    private suspend fun processIptvChannel(scheduler: Scheduler, channelId: String, streamUrl: String) {
        // channel with valid IPTV stream URL and allow to generate Catchup content
        // Get the current date and time
        val startAt = LocalDateTime.now()
        // Calculate the end time for today (end of the day)
        val endAt = startAt.toLocalDate().atTime(23, 59, 59)
        // Fetch EPG channel programmes for the specified date range
        val programmes = epgChannelProgrammeDataSource.findByChannelIdAndDateRange(channelId, startAt, endAt)
        for (programme in programmes) {
            val programCatchupJob = ProgramCatchupGenerationJob.buildJob(
                jobId = ProgramCatchupGenerationJob.DEFAULT_JOB_ID.plus("_${programme.id}"),
                data = mapOf(
                    ProgramCatchupGenerationJob.EPG_CHANNEL_PROGRAMME_ID_ARG to programme.id.toString(),
                    ProgramCatchupGenerationJob.CHANNEL_STREAM_URL_ARG to streamUrl
                )
            )
            val trigger = ProgramCatchupGenerationJob.buildTrigger(ProgramCatchupGenerationJob.DEFAULT_TRIGGER_ID.plus("_${programme.id}"))
            scheduler.scheduleJob(programCatchupJob, trigger)
        }
    }

    companion object : IJobBuilder {

        // Default job ID for the CatchupIptvChannelsJob
        private const val DEFAULT_JOB_ID = "catchup_iptv_channels_job"
        private const val DEFAULT_TRIGGER_ID = "ingest_languages_job_trigger"
        private const val INTERVAL_IN_MINUTES = 5

        /**
         * Build a JobDetail instance for the CatchupIptvChannelsJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the CatchupIptvChannelsJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<CatchupIptvChannelsJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Build a Trigger instance for the CatchupIptvChannelsJob.
         *
         * @param triggerId The optional custom trigger ID.
         * @return A Trigger instance for the CatchupIptvChannelsJob.
         */
        override fun buildTrigger(triggerId: String?): Trigger =
            createNewTrigger(triggerId ?: DEFAULT_TRIGGER_ID, INTERVAL_IN_MINUTES)

        /**
         * Get the JobKey for the CatchupIptvChannelsJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the CatchupIptvChannelsJob.
         */
        override fun getJobKey(jobId: String?): JobKey =
            createJobKey(jobId ?: DEFAULT_JOB_ID)

        /**
         * Get the interval (in minutes) at which the job should be executed.
         *
         * @return The execution interval in minutes.
         */
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}
