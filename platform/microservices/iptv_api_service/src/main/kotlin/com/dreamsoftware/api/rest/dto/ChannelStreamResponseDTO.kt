package com.dreamsoftware.api.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelStreamResponseDTO(

    /**
     * The code of the channel stream.
     */
    @SerialName("code")
    val code: String,
    /**
     * The URL of the channel stream.
     */
    @SerialName("url")
    val url: String,

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
