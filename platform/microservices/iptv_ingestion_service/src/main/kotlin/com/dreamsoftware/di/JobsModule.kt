package com.dreamsoftware.di

import com.dreamsoftware.tasks.core.manager.JobSchedulerManager
import com.dreamsoftware.tasks.core.manager.JobSchedulerManagerImpl
import com.dreamsoftware.model.DatabaseConfig
import com.dreamsoftware.tasks.core.JobFactoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import org.quartz.spi.JobFactory
import java.util.*

val jobsModule = module {
    includes(configModule)
    factory(named("quartzProperties")) {
        with(get<DatabaseConfig>()) {
            Properties().apply {
                setProperty("org.quartz.scheduler.instanceName", "IptvJobsScheduler")
                setProperty("org.quartz.threadPool.threadCount", "3")
                setProperty("org.quartz.jobStore.dataSource", "mySql")
                setProperty("org.quartz.dataSource.mySql.driver", driverClass)
                setProperty("org.quartz.dataSource.mySql.URL", url)
                setProperty("org.quartz.dataSource.mySql.user", user)
                setProperty("org.quartz.dataSource.mySql.password", password)
                setProperty("org.quartz.dataSource.mySql.maxConnections", "10")
                setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX")
                setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate")
                setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_")
                setProperty("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingTriggerHistoryPlugin")
                setProperty("org.quartz.plugin.triggHistory.triggerFiredMessage", """Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}""")
                setProperty("org.quartz.plugin.triggHistory.triggerCompleteMessage", """Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy}""")
            }
        }
    }
    single<JobFactory> {
        JobFactoryImpl()
    }
    single<Scheduler> {
        StdSchedulerFactory(get<Properties>(named("quartzProperties"))).scheduler.also {
            it.setJobFactory(get())
        }
    }
    single<JobSchedulerManager> {
        JobSchedulerManagerImpl(get())
    }
}