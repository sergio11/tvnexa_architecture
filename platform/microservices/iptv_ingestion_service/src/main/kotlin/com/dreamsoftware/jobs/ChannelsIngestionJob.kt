package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobKey

@DisallowConcurrentExecution
class ChannelsIngestionJob(
    private val channelsNetworkDataSource: IptvOrgNetworkDataSource<ChannelDTO>,
    private val channelsMapper: IMapper<ChannelDTO, SaveChannelEntity>,
    private val channelsDatabaseDataSource: IChannelDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?) {
        val channels = channelsNetworkDataSource.fetchContent()
        log.debug("${channels.count()} channels will be processed")
        channelsDatabaseDataSource.save(channelsMapper.mapList(channels))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_channels_job"
        private const val INTERVAL_IN_MINUTES = 2

        override fun buildJob(): JobDetail = createNewJob<ChannelsIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKeys(): Iterable<JobKey> = listOf(
            LanguagesIngestionJob.getJobKey(),
            CategoriesIngestionJob.getJobKey(),
            SubdivisionsIngestionJob.getJobKey()
        )
    }
}