package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.stream.IStreamDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelStreamDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

/**
 * ChannelStreamsIngestionJob is a Quartz Job that fetches and ingests channel stream data from an external source
 * into the database.
 *
 * @param streamsNetworkDataSource The data source for fetching channel stream data.
 * @param streamsMapper Mapper for converting ChannelStreamDTO to SaveChannelStreamEntity.
 * @param streamsDatabaseDataSource The database data source for storing channel stream data.
 */
@DisallowConcurrentExecution
class ChannelStreamsIngestionJob(
    private val streamsNetworkDataSource: IptvOrgNetworkDataSource<ChannelStreamDTO>,
    private val streamsMapper: ISimpleMapper<ChannelStreamDTO, SaveChannelStreamEntity>,
    private val streamsDatabaseDataSource: IStreamDatabaseDataSource
) : SupportJob() {

    /**
     * This method is called when the job starts its execution.
     *
     * @param jobData The optional JobDataMap containing job-specific data.
     * @param scheduler The Quartz scheduler instance.
     */
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting ChannelStreamsIngestionJob execution.")

        try {
            // Fetch channel stream data from an external source
            val streams = streamsNetworkDataSource.fetchContent().filter { it.channel != null }

            // Log the number of channel streams to be processed
            log.debug("${streams.count()} channel streams will be processed")

            // Save the fetched channel stream data to the database
            streamsDatabaseDataSource.save(streamsMapper.mapList(streams))

            log.debug("ChannelStreamsIngestionJob execution completed successfully.")
        } catch (e: Exception) {
            log.error("Error during ChannelStreamsIngestionJob execution: ${e.message}", e)
        }
    }

    companion object : IJobBuilder {

        // Default job ID and interval for execution (in minutes)
        private const val DEFAULT_JOB_ID = "channels_streams_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 2

        /**
         * Build a JobDetail instance for the ChannelStreamsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @param data The optional job-specific data.
         * @return A JobDetail instance for the ChannelStreamsIngestionJob.
         */
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<ChannelStreamsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        /**
         * Get the JobKey for the ChannelStreamsIngestionJob.
         *
         * @param jobId The optional custom job ID.
         * @return The JobKey for the ChannelStreamsIngestionJob.
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
         * Get the parent job key that this job depends on.
         *
         * @return The JobKey of the parent job.
         */
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}