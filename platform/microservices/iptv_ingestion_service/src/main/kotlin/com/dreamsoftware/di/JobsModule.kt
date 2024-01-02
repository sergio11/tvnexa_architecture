package com.dreamsoftware.di

import com.dreamsoftware.core.getMapper
import com.dreamsoftware.data.database.core.IDbMigrationConfig
import com.dreamsoftware.data.iptvorg.di.*
import com.dreamsoftware.jobs.*
import com.dreamsoftware.jobs.core.JobFactoryImpl
import com.dreamsoftware.jobs.core.manager.IJobSchedulerManager
import com.dreamsoftware.jobs.core.manager.impl.JobSchedulerManagerImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import org.quartz.spi.JobFactory
import java.util.*

val jobsModule = module {
    includes(configModule)

    // Define properties for Quartz scheduler configuration
    factory(named("quartzProperties")) {
        Properties().apply {
            // Quartz Configuration
            setProperty("org.quartz.scheduler.instanceName", "IptvJobsScheduler")
            setProperty("org.quartz.scheduler.instanceId", "AUTO")
            setProperty("org.quartz.threadPool.threadCount", "3")
            setProperty("org.quartz.jobStore.dataSource", "ShareHikariCP")
            setProperty("org.quartz.dataSource.ShareHikariCP.connectionProvider.class", "com.dreamsoftware.jobs.core.QuartzJobsConnectionProvider")
            setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX")
            setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate")
            setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_")
            setProperty(
                "org.quartz.plugin.triggHistory.class",
                "org.quartz.plugins.history.LoggingTriggerHistoryPlugin"
            )
            setProperty(
                "org.quartz.plugin.triggHistory.triggerFiredMessage",
                """Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}"""
            )
            setProperty(
                "org.quartz.plugin.triggHistory.triggerCompleteMessage",
                """Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy}"""
            )
            setProperty("org.quartz.jobStore.isClustered", "true")
            setProperty("org.quartz.jobStore.clusterCheckinInterval", "15000")
        }
    }

    // Define JobFactory for Quartz jobs
    single<JobFactory> {
        JobFactoryImpl()
    }

    // Create a Scheduler with Quartz scheduler factory
    single<Scheduler> {
        StdSchedulerFactory(get<Properties>(named("quartzProperties"))).scheduler.also {
            it.setJobFactory(get())
        }
    }

    // Define a Job Scheduler Manager
    single<IJobSchedulerManager> {
        JobSchedulerManagerImpl(get())
    }

    // Define DB migration configuration
    single {
        object : IDbMigrationConfig {
            override val schemaTableName: String
                get() = "quartz_schema_history"
            override val schemaLocation: String?
                get() = null
            override val genericSchema: Boolean
                get() = false
        }
    } bind IDbMigrationConfig::class

    // Define various jobs for ingestion
    factory { LanguagesIngestionJob(get(named(LANGUAGES_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { CategoriesIngestionJob(get(named(CATEGORIES_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { CountriesIngestionJob(get(named(COUNTRIES_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { RegionsIngestionJob(get(named(REGIONS_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { SubdivisionsIngestionJob(get(named(SUBDIVISIONS_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { ChannelsIngestionJob(get(named(CHANNELS_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { ChannelGuidesIngestionJob(get(named(CHANNEL_GUIDES_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { ChannelStreamsIngestionJob(get(named(CHANNEL_STREAMS_NETWORK_DATA_SOURCE)), getMapper(), get()) }
    factory { EpgGrabbingJob(get(), get(), get(), getMapper()) }
}