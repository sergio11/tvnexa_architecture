package com.dreamsoftware.data.ftp.datasource.impl

import com.dreamsoftware.data.ftp.datasource.IConfigFtpDataSource
import com.dreamsoftware.data.ftp.model.FtpConfig
import org.apache.commons.io.FileUtils
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.File
import java.nio.file.Files

class ConfigFtpDataSourceImpl(
    private val ftpClient: FTPClient,
    private val ftpConfig: FtpConfig
): IConfigFtpDataSource {

    override suspend fun getConfig(): String = with(ftpConfig) {
        var downloadedFile: File? = null
        with(ftpClient) {
            try {
                println("try to connect to FTP Host: $hostname")
                connect("192.168.1.39")
                enterLocalPassiveMode()
                login(user, password)
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    disconnect()
                }
                println("try to download file ${fileName}.${fileExt}")
                val remoteFile = retrieveFileStream("$fileName.$fileExt")
                println("copy to local file")
                downloadedFile = File(Files.createTempDirectory(null).toFile(), "$fileName.$fileExt")
                FileUtils.copyInputStreamToFile(remoteFile, downloadedFile)
                logout()
            } catch (ex: Exception) {
                ex.printStackTrace()
                println("Ex occurred -> ${ex.message}")
            } finally {
                if (isConnected) {
                    disconnect()
                }
            }
            println("Downloaded file path -> ${downloadedFile?.absolutePath}")
            downloadedFile?.absolutePath ?: throw IllegalStateException("File can not be downloaded")
        }
    }
}