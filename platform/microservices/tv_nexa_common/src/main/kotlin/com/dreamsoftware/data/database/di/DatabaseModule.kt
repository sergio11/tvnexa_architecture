package com.dreamsoftware.data.database.di

import com.dreamsoftware.data.database.core.DatabaseFactoryImpl
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.core.IDbMigrationConfig
import com.dreamsoftware.model.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import javax.sql.DataSource

const val WRITE_DATA_SOURCE = "writeDataSource"
const val READ_DATA_SOURCE = "readDataSource"

val databaseModule = module {

    factory {
        with(get<DatabaseConfig>()) {
            HikariConfig().apply {
                driverClassName = driverClass
                username = connUser
                password = connPassword
                isAutoCommit = false
                maxLifetime = 600000 // 10 minutes
                idleTimeout = 540000 // 9 minutes
            }
        }
    }

    single<DataSource>(named(READ_DATA_SOURCE)) {
        with(get<DatabaseConfig>().readGroupConfig) {
            HikariDataSource(get<HikariConfig>().apply {
                jdbcUrl = databaseUrl
                maximumPoolSize = maxPoolSize
                poolName = "TvNexaReadDataSourcePool"
                validate()
            })
        }
    }

    single<DataSource>(named(WRITE_DATA_SOURCE)) {
        with(get<DatabaseConfig>().writeGroupConfig) {
            HikariDataSource(get<HikariConfig>().apply {
                jdbcUrl = databaseUrl
                maximumPoolSize = maxPoolSize
                poolName = "TvNexaWriteDataSourcePool"
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
        DatabaseFactoryImpl(get(named(WRITE_DATA_SOURCE)), get(named(READ_DATA_SOURCE)), getAll()).also {
            it.connectAndMigrate()
        }
    }
}