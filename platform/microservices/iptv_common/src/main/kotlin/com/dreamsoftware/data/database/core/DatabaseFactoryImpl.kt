package com.dreamsoftware.data.database.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class DatabaseFactoryImpl(
    private val datasource: DataSource
): IDatabaseFactory {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun connectAndMigrate() {
        Database.connect(datasource)
        runFlyway(datasource)
    }

    private fun runFlyway(datasource: DataSource) {
        with(Flyway.configure().dataSource(datasource).load()) {
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

    suspend fun <T> dbExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}