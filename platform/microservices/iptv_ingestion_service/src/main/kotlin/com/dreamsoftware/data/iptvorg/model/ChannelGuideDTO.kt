package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelGuideDTO(
    @SerialName("channel")
    val channel: String,
    @SerialName("site")
    val site: String,
    @SerialName("lang")
    val lang: String,
    @SerialName("days")
    val days: Int
)
