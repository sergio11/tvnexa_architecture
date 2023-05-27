package com.dreamsoftware.data.database.core

import org.jetbrains.exposed.sql.Transaction

interface IDatabaseFactory {
    fun connectAndMigrate()
    suspend fun <T> dbExec(block: Transaction.() -> T): T
}