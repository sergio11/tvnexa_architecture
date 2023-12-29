package com.dreamsoftware.data.database.entity

data class ChannelGuideEntity(
    val id: Long,
    val site: String,
    val siteId: String,
    val siteName: String,
    val channel: ChannelEntity?,
    val lang: String
)

data class SaveChannelGuideEntity(
    val site: String,
    val siteId: String,
    val siteName: String,
    val channel: String?,
    val lang: String
)
