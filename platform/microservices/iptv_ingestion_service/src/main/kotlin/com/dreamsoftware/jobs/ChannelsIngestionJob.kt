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

@DisallowConcurrentExecution
class ChannelsIngestionJob(
    private val channelsNetworkDataSource: IptvOrgNetworkDataSource<ChannelDTO>,
    private val channelsMapper: ISimpleMapper<ChannelDTO, SaveChannelEntity>,
    private val channelsDatabaseDataSource: IChannelDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val channels = channelsNetworkDataSource.fetchContent()
        log.debug("${channels.count()} channels will be processed")
        channelsDatabaseDataSource.save(channelsMapper.mapList(channels))
    }

    companion object: IJobBuilder {

        private const val DEFAULT_JOB_ID = "ingest_channels_job"
        private const val INTERVAL_IN_MINUTES = 2

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<ChannelsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKeys(): Iterable<JobKey> = listOf(
            LanguagesIngestionJob.getJobKey(),
            CategoriesIngestionJob.getJobKey(),
            SubdivisionsIngestionJob.getJobKey()
        )
    }
}