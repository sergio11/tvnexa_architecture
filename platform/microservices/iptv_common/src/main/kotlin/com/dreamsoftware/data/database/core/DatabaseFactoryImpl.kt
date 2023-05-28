package com.dreamsoftware.data.database.core

import com.dreamsoftware.core.isDevelopmentMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import javax.sql.DataSource

internal class DatabaseFactoryImpl(
    private val datasource: DataSource,
    private val schemaConfigList: List<IDbMigrationConfig>
) : IDatabaseFactory {

    private companion object {
        const val DEV_MIGRATION_FOLDER = "/dev"
        const val PROD_MIGRATION_FOLDER = "/prod"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    private val envMigrationFolder by lazy {
        if (isDevelopmentMode) {
            DEV_MIGRATION_FOLDER
        } else {
            PROD_MIGRATION_FOLDER
        }
    }

    override fun connectAndMigrate() {
        log.debug("DatabaseFactory - connectAndMigrate start")
        Database.connect(datasource)
        runFlyway(datasource)
    }

    override suspend fun <T> dbExec(disableFkValidations: Boolean, block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        if (!isConnected()) {
            Database.connect(datasource)
        }
        transaction {
            if(disableFkValidations) {
                exec("SET foreign_key_checks = 0")
            }
            block().also {
                if(disableFkValidations) {
                    exec("SET foreign_key_checks = 1")
                }
            }
        }
    }

    private fun runFlyway(datasource: DataSource) {
        log.debug("DatabaseFactory - runFlyway migrations folder - $envMigrationFolder - size ${schemaConfigList.size}")
        schemaConfigList.map { config ->
            with(config) {
                Flyway.configure().apply {
                    sqlMigrationPrefix("v")
                    sqlMigrationSeparator("__")
                    sqlMigrationSuffixes(".sql")
                    validateMigrationNaming(true)
                    baselineOnMigrate(true)
                    baselineVersion("0")
                    if (!schemaTableName.isNullOrBlank()) {
                        table(schemaTableName)
                    }
                    if(genericSchema) {
                        if (!schemaLocation.isNullOrBlank()) {
                            locations(schemaLocation)
                        }
                    } else {
                        if (!schemaLocation.isNullOrBlank()) {
                            locations(schemaLocation.plus(envMigrationFolder))
                        } else {
                            locations(*locations.map {
                                it.path.plus(envMigrationFolder)
                            }.toTypedArray())
                        }
                    }
                }.dataSource(datasource).load()
            }
        }.forEach { flyway ->
            with(flyway) {
                try {
                    log.info(
                        "Flyway migration ${configuration.table} starting, check migrations at ${
                            configuration.locations.joinToString(
                                ","
                            )
                        }"
                    )
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