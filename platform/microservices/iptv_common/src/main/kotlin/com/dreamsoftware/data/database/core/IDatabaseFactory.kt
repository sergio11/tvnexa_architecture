package com.dreamsoftware.data.database.core

interface IDatabaseFactory {
    fun connectAndMigrate()
    suspend fun <T> dbExec(block: () -> T): T
}