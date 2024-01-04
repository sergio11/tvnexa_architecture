package com.dreamsoftware.api.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleChannelResponseDTO(
    @SerialName("channel_id")
    val channelId: String,

    /**
     * The name of the channel.
     */
    @SerialName("name")
    val name: String? = null,

    /**
     * The city or location associated with the channel (if applicable).
     */
    @SerialName("city")
    val city: String? = null,

    /**
     * Indicates whether the channel contains NSFW (Not Safe For Work) content.
     */
    @SerialName("is_nsfw")
    val isNsfw: Boolean? = null,

    /**
     * The website URL associated with the channel.
     */
    @SerialName("website")
    val website: String? = null,

    /**
     * The URL to the channel's logo or image.
     */
    @SerialName("logo")
    val logo: String? = null,
)