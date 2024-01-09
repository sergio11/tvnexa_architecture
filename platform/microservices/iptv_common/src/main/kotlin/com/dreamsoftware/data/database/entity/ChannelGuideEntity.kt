package com.dreamsoftware.data.database.entity

data class ChannelGuideEntity(
    val code: String,
    val site: String,
    val siteId: String,
    val siteName: String,
    val channel: SimpleChannelEntity,
    val lang: String
)

data class ChannelGuideAggregateEntity(
    val site: String,
    val lang: String,
    val count: Long
)

data class SaveChannelGuideEntity(
    val code: String,
    val site: String,
    val siteId: String,
    val siteName: String,
    val channel: String,
    val lang: String
)
