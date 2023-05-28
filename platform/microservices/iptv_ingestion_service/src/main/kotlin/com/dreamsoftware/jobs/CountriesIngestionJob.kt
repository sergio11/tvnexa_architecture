package com.dreamsoftware.jobs

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.model.CountryDTO
import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.SupportJob
import com.dreamsoftware.jobs.core.createJobKey
import com.dreamsoftware.jobs.core.createNewJob
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobKey

@DisallowConcurrentExecution
class CountriesIngestionJob(
    private val countriesNetworkDataSource: IptvOrgNetworkDataSource<CountryDTO>,
    private val countriesMapper: IMapper<CountryDTO, SaveCountryEntity>,
    private val countriesDatabaseDataSource: ICountryDatabaseDataSource
): SupportJob() {

    override suspend fun onStartExecution(jobData: JobDataMap?) {
        val countries = countriesNetworkDataSource.fetchContent()
        log.debug("${countries.count()} countries will be processed")
        countriesDatabaseDataSource.save(countriesMapper.mapList(countries))
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "countries_ingestion_job"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(): JobDetail = createNewJob<CountriesIngestionJob>(JOB_ID)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
        override fun getParentJobKey(): JobKey = LanguagesIngestionJob.getJobKey()
    }
}