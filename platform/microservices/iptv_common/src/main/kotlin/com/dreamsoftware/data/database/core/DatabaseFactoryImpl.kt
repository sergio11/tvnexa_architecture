package com.dreamsoftware.data.database.core

import com.dreamsoftware.core.isDevelopmentMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

/**
 * DatabaseFactoryImpl is responsible for database connection and migration management.
 * It utilizes Flyway for schema migration and manages database connections using HikariCP.
 *
 * @param writeDatasource The data source for write database connections.
 * @param readDatasource The data source for read database connections.
 * @param schemaConfigList A list of database migration configuration objects.
 */
internal class DatabaseFactoryImpl(
    private val writeDatasource: DataSource,
    private val readDatasource: DataSource,
    private val schemaConfigList: List<IDbMigrationConfig>
) : IDatabaseFactory {

    private companion object {
        const val DEV_MIGRATION_FOLDER = "/dev"
        const val PROD_MIGRATION_FOLDER = "/prod"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    // Determine the appropriate migration folder based on the environment
    private val envMigrationFolder by lazy {
        if (isDevelopmentMode) {
            DEV_MIGRATION_FOLDER
        } else {
            PROD_MIGRATION_FOLDER
        }
    }

    /**
     * Connects to the database and performs schema migration using Flyway.
     */
    override fun connectAndMigrate() {
        log.debug("DatabaseFactory - connectAndMigrate start")
        connectIfNotConnected(writeDatasource)
        // Run schema migrations
        runFlyway(writeDatasource)
    }

    /**
     * Executes a database operation within a transaction.
     *
     * @param block The database operation to execute within the transaction.
     * @return The result of the database operation.
     */
    override suspend fun <T> execReadableTransaction(block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        connectIfNotConnected(readDatasource)
        transaction {
            block()
        }
    }

    /**
     * Executes a database operation within a transaction. Optionally, foreign key validations can be disabled.
     *
     * @param disableFkValidations Flag to disable foreign key validations during the transaction.
     * @param block The database operation to execute within the transaction.
     * @return The result of the database operation.
     */
    override suspend fun <T> execWritableTransaction(disableFkValidations: Boolean, block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        connectIfNotConnected(writeDatasource)
        transaction {
            if(disableFkValidations) {
                // Disable foreign key checks if requested
                exec("SET foreign_key_checks = 0")
            }
            block().also {
                if(disableFkValidations) {
                    // Re-enable foreign key checks after the transaction
                    exec("SET foreign_key_checks = 1")
                }
            }
        }
    }

    /**
     * Runs Flyway database migrations using the provided data source and migration configurations.
     *
     * @param datasource The data source to use for migrations.
     */
    private fun runFlyway(datasource: DataSource) {
        log.debug("DatabaseFactory - runFlyway migrations folder - $envMigrationFolder - size ${schemaConfigList.size}")

        schemaConfigList.map { config ->
            with(config) {
                // Configure Flyway migration settings
                Flyway.configure().apply {
                    sqlMigrationPrefix("v")
                    sqlMigrationSeparator("__")
                    sqlMigrationSuffixes(".sql")
                    validateMigrationNaming(true)
                    baselineOnMigrate(true)
                    baselineVersion("0")

                    if (!schemaTableName.isNullOrBlank()) {
                        // Set the custom schema table name if provided
                        table(schemaTableName)
                    }

                    if(genericSchema) {
                        if (!schemaLocation.isNullOrBlank()) {
                            // Use the specified schema location for generic schema
                            locations(schemaLocation)
                        }
                    } else {
                        if (!schemaLocation.isNullOrBlank()) {
                            // Use the schema location with the appropriate environment folder
                            locations(schemaLocation.plus(envMigrationFolder))
                        } else {
                            // Append the environment folder to all specified locations
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
                    // Perform Flyway migration for each schema configuration
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

    /**
     * Connects to the database if it is not connected.
     *
     * @param dataSource The data source to connect to.
     */
    private fun connectIfNotConnected(dataSource: DataSource) {
        // Connect to the data source if not already connected
        Database.connect(dataSource)
    }
}