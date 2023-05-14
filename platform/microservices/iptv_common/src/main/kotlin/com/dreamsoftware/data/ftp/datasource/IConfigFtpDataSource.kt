package com.dreamsoftware.data.ftp.datasource

interface IConfigFtpDataSource {
    suspend fun getConfig(): String
}