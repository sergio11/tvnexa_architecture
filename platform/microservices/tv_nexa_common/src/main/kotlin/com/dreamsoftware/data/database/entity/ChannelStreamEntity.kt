package com.dreamsoftware.data.database.entity


data class ChannelStreamEntity(
    val code: String,
    val url: String,
    val httpReferrer: String? = null,
    val userAgent: String? = null
)

data class SaveChannelStreamEntity(
    val code: String,
    val url: String,
    val channelId: String,
    val httpReferrer: String? = null,
    val userAgent: String? = null
)
