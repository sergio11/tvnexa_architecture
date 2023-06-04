package com.dreamsoftware.data.epg.model

data class TestEpgDTO(
    val channelId: String,
    val title: String,
    val start: String,
    val stop: String,
    val date: String,
    val category: String? = null
)
