package com.dreamsoftware.jobs.core

import com.zaxxer.hikari.HikariDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.quartz.utils.ConnectionProvider
import java.sql.Connection
import javax.sql.DataSource

class QuartzJobsConnectionProvider: ConnectionProvider, KoinComponent {

    private val dataSource: DataSource by inject()

    var password: String? = null

    override fun getConnection(): Connection =
        dataSource.connection.also {
            println("QuartzJobsConnectionProvider getConnection CALLED!")
        }

    override fun shutdown() {
        (dataSource as? HikariDataSource)?.close()?.also {
            println("QuartzJobsConnectionProvider shutdown CALLED!")
        }
    }

    override fun initialize() {
        println("QuartzJobsConnectionProvider initialize CALLED!")
    }
}