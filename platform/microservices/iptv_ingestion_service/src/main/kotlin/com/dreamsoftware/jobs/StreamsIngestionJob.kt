package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.stream.IStreamDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveStreamEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.StreamDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.JobDetail
import org.quartz.JobKey

class StreamsIngestionJob(
    private val streamsNetworkDataSource: IptvOrgNetworkDataSource<StreamDTO>,
    private val streamsMapper: IMapper<StreamDTO, SaveStreamEntity>,
    private val streamsDatabaseDataSource: IStreamDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val regions = streamsNetworkDataSource.fetchContent()
        streamsDatabaseDataSource.save(streamsMapper.mapList(regions))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_streams_job"

        override fun buildJob(): JobDetail = createNewJob<StreamsIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}