package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.SubdivisionDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.JobDetail
import org.quartz.JobKey

class SubdivisionsIngestionJob(
    private val subdivisionsNetworkDataSource: IptvOrgNetworkDataSource<SubdivisionDTO>,
    private val subdivisionsMapper: IMapper<SubdivisionDTO, SaveSubdivisionEntity>,
    private val subdivisionsDatabaseDataSource: ISubdivisionDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution() {
        val subdivisions = subdivisionsNetworkDataSource.fetchContent()
        subdivisionsDatabaseDataSource.save(subdivisionsMapper.mapList(subdivisions))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "ingest_subdivisions_job"

        override fun buildJob(): JobDetail = createNewJob<SubdivisionsIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getParentJobKey(): JobKey = CountriesIngestionJob.getJobKey()
    }
}