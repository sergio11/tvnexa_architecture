package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

/**
 * ChannelsIngestionJob is a Quartz Job that fetches and ingests channel data from an external source
 * into the database.
 *
 * @param channelsNetworkDataSource The data source for fetching channel data.
 * @param channelsMapper Mapper for converting ChannelDTO to SaveChannelEntity.
 * @param channelsDatabaseDataSource The database data source for storing channel data.
 */
@DisallowConcurrentExecution
class ChannelsIngestionJob(
    private val channelsNetworkDataSource: IptvOrgNetworkDataSource<ChannelDTO>,
    private val channelsMapper: ISimpleMapper<ChannelDTO, SaveChannelEntity>,
    private val channelsDatabaseDataSource: IChannelDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting ChannelsIngestionJob execution.")

        try {
            // Fetch channel data from an external source
            val channels = channelsNetworkDataSource.fetchContent()

            // Log the number of channels to be processed
            log.debug("${channels.count()} channels will be processed")

            // Save the fetched channel data to the database
            channelsDatabaseDataSource.save(channelsMapper.mapList(channels))

            log.debug("ChannelsIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during ChannelsIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "ingest_channels_job"
        private const val INTERVAL_IN_MINUTES = 2

        /**
         * Build a JobDetail instance for the ChannelsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the ChannelsIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<ChannelsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Get the JobKey for the ChannelsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the ChannelsIngestionJob.
         */
        override fun getJobKey(jobId: String?): JobKey =
            createJobKey(jobId ?: DEFAULT_JOB_ID)

        /**
         * Get the interval (in minutes) at which the job should be executed.
         *
         * @return The execution interval in minutes.
         */
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES

        /**
         * Get the parent job keys that this job depends on.
         *
         * @return A list of JobKey objects representing the parent jobs.
         */
        override fun getParentJobKeys(): Iterable<JobKey> = listOf(
            LanguagesIngestionJob.getJobKey(),
            CategoriesIngestionJob.getJobKey(),
            SubdivisionsIngestionJob.getJobKey()
        )
    }
}