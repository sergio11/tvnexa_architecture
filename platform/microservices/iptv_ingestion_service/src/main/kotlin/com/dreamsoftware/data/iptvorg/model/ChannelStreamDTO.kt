package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelStreamDTO(
    @SerialName("channel")
    val channel: String? = null,
    @SerialName("url")
    val url: String,
    @SerialName("http_referrer")
    val httpReferrer: String? = null,
    @SerialName("user_agent")
    val userAgent: String? = null
)
