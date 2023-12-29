package com.dreamsoftware.data.configuration.model

data class FtpConfig(
    val hostname: String,
    val user: String,
    val password: String,
    val fileName: String,
    val fileExt: String
)
