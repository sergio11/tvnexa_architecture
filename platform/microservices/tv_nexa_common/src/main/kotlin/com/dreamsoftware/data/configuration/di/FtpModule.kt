package com.dreamsoftware.data.configuration.di

import com.dreamsoftware.data.configuration.datasource.IConfigurationDataSource
import com.dreamsoftware.data.configuration.datasource.impl.ConfigurationDataSourceImpl
import com.dreamsoftware.data.configuration.model.FtpConfig
import org.apache.commons.net.ftp.FTPClient
import org.koin.dsl.module

private const val FTP_HOSTNAME = "ftp_repository"
private const val FTP_USER = "dev"
private const val FTP_PASSWORD = "dev"
private const val CONFIG_FILE_NAME = "application"
private const val CONFIG_FILE_EXT = "yml"

val ftpModule = module {
    factory { FTPClient() }
    factory { FtpConfig(
        hostname = FTP_HOSTNAME,
        user = FTP_USER,
        password = FTP_PASSWORD,
        fileName = CONFIG_FILE_NAME,
        fileExt = CONFIG_FILE_EXT
    ) }
    factory<IConfigurationDataSource> { ConfigurationDataSourceImpl(get(), get()) }
}