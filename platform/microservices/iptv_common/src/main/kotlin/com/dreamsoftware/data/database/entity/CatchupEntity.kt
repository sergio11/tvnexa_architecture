package com.dreamsoftware.data.database.entity

import java.time.LocalDateTime

data class CatchupEntity(
    val catchupId: Int,
    val channelId: String,
    val programId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val catchupFilePath: String
)

data class SaveCatchupEntity(
    val channelId: String,
    val programId: String,
    val catchupFilePath: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

