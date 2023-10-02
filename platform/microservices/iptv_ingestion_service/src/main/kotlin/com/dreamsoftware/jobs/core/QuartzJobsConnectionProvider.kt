package com.dreamsoftware.jobs.core

import com.dreamsoftware.data.database.di.WRITE_DATA_SOURCE
import com.zaxxer.hikari.HikariDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.quartz.utils.ConnectionProvider
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.sql.DataSource

/**
 * QuartzJobsConnectionProvider is a custom Quartz ConnectionProvider implementation that integrates
 * with HikariCP DataSource for Quartz job execution.
 *
 * It provides database connections for Quartz jobs using the configured DataSource.
 */
class QuartzJobsConnectionProvider : ConnectionProvider, KoinComponent {

    private val log = LoggerFactory.getLogger(this::class.java)

    // Injecting the DataSource using Koin
    private val dataSource: DataSource by inject(named(WRITE_DATA_SOURCE))

    /**
     * Get a database connection for Quartz job execution.
     *
     * @return A database connection.
     */
    override fun getConnection(): Connection =
        dataSource.connection.also {
            log.debug("QuartzJobsConnectionProvider getConnection CALLED!")
        }

    /**
     * Shutdown the QuartzJobsConnectionProvider. This method is called when Quartz is shutting down.
     */
    override fun shutdown() {
        (dataSource as? HikariDataSource)?.close()?.also {
            log.debug("QuartzJobsConnectionProvider shutdown CALLED!")
        }
    }

    /**
     * Initialize the QuartzJobsConnectionProvider. This method is called when Quartz is initialized.
     */
    override fun initialize() {
        log.debug("QuartzJobsConnectionProvider initialize CALLED!")
    }
}