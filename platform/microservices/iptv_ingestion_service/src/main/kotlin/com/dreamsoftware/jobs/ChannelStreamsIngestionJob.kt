package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.stream.IStreamDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelStreamDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.JobDetail
import org.quartz.JobKey

class ChannelStreamsIngestionJob(
    private val streamsNetworkDataSource: IptvOrgNetworkDataSource<ChannelStreamDTO>,
    private val streamsMapper: IMapper<ChannelStreamDTO, SaveChannelStreamEntity>,
    private val streamsDatabaseDataSource: IStreamDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val streams = streamsNetworkDataSource.fetchContent()
        streamsDatabaseDataSource.save(streamsMapper.mapList(streams))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_streams_job"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(): JobDetail = createNewJob<ChannelStreamsIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}