package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelGuideDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobKey

@DisallowConcurrentExecution
class ChannelGuidesIngestionJob(
    private val guidesNetworkDataSource: IptvOrgNetworkDataSource<ChannelGuideDTO>,
    private val guidesMapper: IMapper<ChannelGuideDTO, SaveChannelGuideEntity>,
    private val guidesDatabaseDataSource: IChannelGuideDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?) {
        val guides = guidesNetworkDataSource.fetchContent()
        guidesDatabaseDataSource.save(guidesMapper.mapList(guides))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "channel_guides_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 5


        override fun buildJob(): JobDetail = createNewJob<ChannelGuidesIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelsIngestionJob.getJobKey()
    }
}