package com.dreamsoftware.data.database.core

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import javax.sql.DataSource

internal class DatabaseFactoryImpl(
    private val datasource: DataSource,
    private val schemaConfigList: List<IDbMigrationConfig>
): IDatabaseFactory {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val  folderMigrationSuffix by lazy {
        if(System.getProperty("development.mode").toBoolean()) {
            "/dev"
        } else {
            "/prod"
        }
    }

    override fun connectAndMigrate() {
        log.debug("DatabaseFactory - connectAndMigrate start")
        Database.connect(datasource)
        runFlyway(datasource)
    }

    override suspend fun <T> dbExec(block: () -> T): T = withContext(Dispatchers.IO) {
        if(!isConnected()) {
            Database.connect(datasource)
        }
        transaction { block() }
    }

    private fun runFlyway(datasource: DataSource) {
        System.getProperties().propertyNames().asIterator().forEach {
            log.debug("Property -> $it")
        }
        log.debug("development.mode -> ${System.getProperty("development.mode")}")
        log.debug("DatabaseFactory - runFlyway migrations folder - $folderMigrationSuffix - size ${schemaConfigList.size}")
        schemaConfigList.map {
            Flyway.configure().apply {
                sqlMigrationPrefix("v")
                sqlMigrationSeparator("__")
                sqlMigrationSuffixes(".sql")
                validateMigrationNaming(true)
                baselineOnMigrate(true)
                baselineVersion("0")
                if(!it.schemaTableName.isNullOrBlank()) {
                    table(it.schemaTableName)
                }
                if(!it.schemaLocation.isNullOrBlank()) {
                    locations(it.schemaLocation.plus(folderMigrationSuffix))
                } else {
                    locations(*locations.map {
                        it.path.plus(folderMigrationSuffix)
                    }.toTypedArray())
                }
            }.dataSource(datasource).load()
        }.forEach { flyway ->
            with(flyway) {
                try {
                    log.info("Flyway migration ${configuration.table} starting, check migrations at ${configuration.locations.joinToString(",")}")
                    info()
                    migrate()
                } catch (e: Exception) {
                    log.error("Exception running flyway migration", e)
                    throw e
                }
                log.info("Flyway migration has finished")
            }
        }
    }

    private fun isConnected() = transaction {
        try {
            !connection.isClosed
        } catch (e: Exception) {
            false
        }
    }
}