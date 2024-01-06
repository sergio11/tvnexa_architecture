package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.ChannelGuideDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

@DisallowConcurrentExecution
class ChannelGuidesIngestionJob(
    private val guidesNetworkDataSource: IptvOrgNetworkDataSource<ChannelGuideDTO>,
    private val guidesMapper: ISimpleMapper<ChannelGuideDTO, SaveChannelGuideEntity>,
    private val guidesDatabaseDataSource: IChannelGuideDatabaseDataSource
) : SupportJob() {

    // This method is called when the job starts its execution
    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        log.debug("Starting ChannelGuidesIngestionJob execution.")
        // Fetch channel guides from an external source
        val guidesAttachedToChannel = guidesNetworkDataSource.fetchContent().filter { it.channel != null }

        // Log the number of channel guides to be processed
        log.debug("${guidesAttachedToChannel.count()} channel guides will be processed")

        // Save the fetched channel guides to the database
        guidesDatabaseDataSource.save(guidesMapper.mapList(guidesAttachedToChannel))

        log.debug("Saved ${guidesAttachedToChannel.count()} channel guides to the database.")
        log.debug("ChannelGuidesIngestionJob execution completed.")
    }

    companion object : IJobBuilder {
        private const val DEFAULT_JOB_ID = "channel_guides_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 2

        // Implement the methods from the IJobBuilder interface
        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail =
            createNewJob<ChannelGuidesIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)

        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = ChannelStreamsIngestionJob.getJobKey()
    }
}