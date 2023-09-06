package com.dreamsoftware.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelStreamResponseDTO(
    /**
     * The URL of the channel stream.
     */
    @SerialName("url")
    val url: String,

    /**
     * The channel information associated with the stream.
     */
    @SerialName("channel")
    val channel: ChannelResponseDTO,

    /**
     * The HTTP referrer header (if provided).
     */
    @SerialName("http_referrer")
    val httpReferrer: String? = null,

    /**
     * The User-Agent header (if provided).
     */
    @SerialName("user_agent")
    val userAgent: String? = null
)
