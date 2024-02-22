package com.dreamsoftware.data.configuration.datasource.impl

import com.dreamsoftware.data.configuration.datasource.IConfigurationDataSource
import com.dreamsoftware.data.configuration.model.FtpConfig
import org.apache.commons.io.FileUtils
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.File
import java.nio.file.Files

/**
 * Implementation of the [IConfigurationDataSource] interface for fetching configuration from an FTP server.
 *
 * @param ftpClient The [FTPClient] instance for connecting to the FTP server.
 * @param ftpConfig The configuration details for the FTP connection.
 */
class ConfigurationDataSourceImpl(
    private val ftpClient: FTPClient,
    private val ftpConfig: FtpConfig
) : IConfigurationDataSource {

    /**
     * Fetches configuration data from the FTP server and returns the local path of the downloaded file.
     *
     * @return The absolute path of the downloaded file.
     * @throws IllegalStateException if the file cannot be downloaded.
     */
    override suspend fun getConfig(): String = with(ftpConfig) {
        var downloadedFile: File? = null
        with(ftpClient) {
            try {
                println("Trying to connect to FTP Host: $hostname")
                connect(hostname)
                enterLocalPassiveMode()
                login(user, password)

                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    disconnect()
                }

                println("Trying to download file ${fileName}.${fileExt}")
                val remoteFile = retrieveFileStream("$fileName.$fileExt")

                println("Copying to local file")
                downloadedFile = File(Files.createTempDirectory(null).toFile(), "$fileName.$fileExt")
                FileUtils.copyInputStreamToFile(remoteFile, downloadedFile)
                logout()
            } catch (ex: Exception) {
                ex.printStackTrace()
                println("Exception occurred -> ${ex.message}")
            } finally {
                if (isConnected) {
                    disconnect()
                }
            }

            println("Downloaded file path -> ${downloadedFile?.absolutePath}")
            downloadedFile?.absolutePath ?: throw IllegalStateException("File cannot be downloaded")
        }
    }
}