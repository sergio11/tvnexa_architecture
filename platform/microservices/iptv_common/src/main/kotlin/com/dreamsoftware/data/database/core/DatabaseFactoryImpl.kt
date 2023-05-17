package com.dreamsoftware.data.database.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import javax.sql.DataSource

internal class DatabaseFactoryImpl(
    private val datasource: DataSource
): IDatabaseFactory {

    private val log = LoggerFactory.getLogger(this::class.java)

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
        log.debug("DatabaseFactory - runFlyway migrations")
        with(Flyway.configure()
            .locations( "db/migration", "classpath:com/dreamsoftware/data/database/migrations")
            .dataSource(datasource)
            .load()
        ) {
            try {
                info()
                migrate()
            } catch (e: Exception) {
                log.error("Exception running flyway migration", e)
                throw e
            }
            log.info("Flyway migration has finished")
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