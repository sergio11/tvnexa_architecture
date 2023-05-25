package com.dreamsoftware.data.database.entity

data class ChannelGuideEntity(
    val id: Long,
    val site: String,
    val channel: ChannelEntity,
    val lang: String,
    val days: Int
)

data class SaveChannelGuideEntity(
    val site: String,
    val channel: String,
    val lang: String,
    val days: Int
)
