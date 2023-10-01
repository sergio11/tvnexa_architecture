package com.dreamsoftware.jobs

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.SubdivisionDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.*

@DisallowConcurrentExecution
class SubdivisionsIngestionJob(
    private val subdivisionsNetworkDataSource: IptvOrgNetworkDataSource<SubdivisionDTO>,
    private val subdivisionsMapper: ISimpleMapper<SubdivisionDTO, SaveSubdivisionEntity>,
    private val subdivisionsDatabaseDataSource: ISubdivisionDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?) {
        val subdivisions = subdivisionsNetworkDataSource.fetchContent()
        log.debug("${subdivisions.count()} subdivisions will be processed")
        subdivisionsDatabaseDataSource.save(subdivisionsMapper.mapList(subdivisions))
    }

    companion object: IJobBuilder {

        private const val DEFAULT_JOB_ID = "subdivisions_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(jobId: String?, data: Map<String, String>?): JobDetail = createNewJob<SubdivisionsIngestionJob>(jobId ?: DEFAULT_JOB_ID, data)
        override fun getJobKey(jobId: String?): JobKey = createJobKey(jobId ?: DEFAULT_JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = CountriesIngestionJob.getJobKey()
    }
}