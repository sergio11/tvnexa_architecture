package com.dreamsoftware.data.database.di

import com.dreamsoftware.data.database.core.DatabaseFactoryImpl
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.core.IDbMigrationConfig
import com.dreamsoftware.model.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.bind
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
    single {
        object : IDbMigrationConfig {
            override val schemaTableName: String
                get() = "iptv_schema_history"
            override val schemaLocation: String
                get() = "classpath:com/dreamsoftware/data/database/migrations"
            override val genericSchema: Boolean
                get() = true
        }
    } bind IDbMigrationConfig::class
    single<IDatabaseFactory>(createdAtStart = true) {
        DatabaseFactoryImpl(get(), getAll()).also {
            it.connectAndMigrate()
        }
    }
}