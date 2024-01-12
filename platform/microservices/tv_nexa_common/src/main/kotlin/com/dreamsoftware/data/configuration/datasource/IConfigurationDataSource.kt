package com.dreamsoftware.data.configuration.datasource

interface IConfigurationDataSource {
    suspend fun getConfig(): String
}