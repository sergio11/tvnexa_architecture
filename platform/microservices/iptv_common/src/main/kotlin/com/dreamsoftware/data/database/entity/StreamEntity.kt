package com.dreamsoftware.data.database.entity


data class StreamEntity(
    val url: String,
    val channel: ChannelEntity,
    val httpReferrer: String? = null,
    val userAgent: String? = null
)

data class SaveStreamEntity(
    val url: String,
    val channelId: String,
    val httpReferrer: String? = null,
    val userAgent: String? = null
)
