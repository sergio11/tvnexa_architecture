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
import org.quartz.JobDetail
import org.quartz.JobKey

class RegionsIngestionJob(
    private val regionsNetworkDataSource: IptvOrgNetworkDataSource<RegionDTO>,
    private val regionMapper: IMapper<RegionDTO, SaveRegionEntity>,
    private val regionsDatabaseDataSource: IRegionDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val regions = regionsNetworkDataSource.fetchContent()
        regionsDatabaseDataSource.save(regionMapper.mapList(regions))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_regions_job"

        override fun buildJob(): JobDetail = createNewJob<RegionsIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getParentJobKey(): JobKey = CountriesIngestionJob.getJobKey()
    }
}