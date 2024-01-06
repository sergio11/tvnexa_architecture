package com.dreamsoftware.data.database.entity

import java.time.LocalDateTime

data class EpgChannelProgrammeEntity(
    val id: Long,
    val title: String,
    val channel: SimpleChannelEntity?,
    val category: CategoryEntity?,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val date: LocalDateTime
)

data class SaveEpgChannelProgrammeEntity(
    val title: String,
    val channel: String?,
    val category: String?,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val date: LocalDateTime
)
