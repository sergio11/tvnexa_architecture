package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a channel guide in an IPTV organization project.
 *
 * @property channel Identifier of the channel.
 * @property site Site or source of the channel guide.
 * @property lang Language of the channel guide.
 * @property days Number of days covered by the guide.
 */
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
