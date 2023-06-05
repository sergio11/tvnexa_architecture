package com.dreamsoftware.data.epg.model

import java.util.*

data class EpgDataDTO(
    val channelId: String,
    val title: String,
    val start: Date,
    val stop: Date,
    val date: Date,
    val category: String? = null
)
