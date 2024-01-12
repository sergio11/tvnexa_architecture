package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a channel stream in an IPTV organization project.
 *
 * @property channel Identifier of the channel associated with the stream.
 * @property url URL of the channel stream.
 * @property httpReferrer HTTP referrer for the stream, if available.
 * @property userAgent User agent for the stream, if available.
 */
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
