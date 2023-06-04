package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveRegionEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.RegionDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

@DisallowConcurrentExecution
class RegionsIngestionJob(
    private val regionsNetworkDataSource: IptvOrgNetworkDataSource<RegionDTO>,
    private val regionMapper: IMapper<RegionDTO, SaveRegionEntity>,
    private val regionsDatabaseDataSource: IRegionDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val regions = regionsNetworkDataSource.fetchContent()
        log.debug("${regions.count()} regions will be processed")
        regionsDatabaseDataSource.save(regionMapper.mapList(regions))
    }

    companion object: IJobBuilder {

        private const val DEFAULT_JOB_ID = "regions_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<RegionsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = CountriesIngestionJob.getJobKey()
    }
}