package com.dreamsoftware.data.database.di

import com.dreamsoftware.data.database.core.DatabaseFactoryImpl
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.model.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import javax.sql.DataSource

val databaseModule = module {
    single<DataSource> {
        with(get<DatabaseConfig>()) {
            HikariDataSource(HikariConfig().apply {
                driverClassName = driverClass
                jdbcUrl = databaseUrl
                username = connUser
                password = connPassword
                maximumPoolSize = maxPoolSize
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            })
        }
    }
    single<IDatabaseFactory> { DatabaseFactoryImpl(get()) }
}