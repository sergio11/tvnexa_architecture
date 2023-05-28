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
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobKey

@DisallowConcurrentExecution
class ChannelStreamsIngestionJob(
    private val streamsNetworkDataSource: IptvOrgNetworkDataSource<ChannelStreamDTO>,
    private val streamsMapper: IMapper<ChannelStreamDTO, SaveChannelStreamEntity>,
    private val streamsDatabaseDataSource: IStreamDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?) {
        val streams = streamsNetworkDataSource.fetchContent()
        log.debug("${streams.count()} channels streams will be processed")
        streamsDatabaseDataSource.save(streamsMapper.mapList(streams))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "channels_streams_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 2

        override fun buildJob(): JobDetail = createNewJob<ChannelStreamsIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}