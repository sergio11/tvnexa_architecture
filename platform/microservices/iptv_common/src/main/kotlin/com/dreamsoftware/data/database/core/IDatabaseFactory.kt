package com.dreamsoftware.data.database.core

interface IDatabaseFactory {
    fun connectAndMigrate()
}