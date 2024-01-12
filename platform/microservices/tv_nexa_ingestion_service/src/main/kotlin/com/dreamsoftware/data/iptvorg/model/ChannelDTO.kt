package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a channel in an IPTV organization project.
 *
 * @property id Unique identifier for the channel.
 * @property name Name of the channel.
 * @property altNames Alternative names or aliases of the channel.
 * @property network Network to which the channel belongs (optional).
 * @property owners Owners or operators of the channel.
 * @property country Country where the channel is based.
 * @property subdivision Subdivision or region within the country (optional).
 * @property city City where the channel is located (optional).
 * @property broadcastArea Areas where the channel is broadcast.
 * @property languages Languages supported by the channel.
 * @property categories Categories or genres of content offered by the channel.
 * @property isNsfw Indicates whether the channel contains NSFW (Not Safe for Work) content.
 * @property launched Date when the channel was launched (optional).
 * @property closed Date when the channel was closed (optional).
 * @property replacedBy Identifier of the channel that replaced this one (optional).
 * @property website Website URL of the channel (optional).
 * @property logo URL of the channel's logo.
 */
@Serializable
data class ChannelDTO(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("alt_names")
    val altNames: List<String>,
    @SerialName("network")
    val network: String? = null,
    @SerialName("owners")
    val owners: List<String>,
    @SerialName("country")
    val country: String,
    @SerialName("subdivision")
    val subdivision: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("broadcast_area")
    val broadcastArea: List<String>,
    @SerialName("languages")
    val languages: List<String>,
    @SerialName("categories")
    val categories: List<String>,
    @SerialName("is_nsfw")
    val isNsfw: Boolean,
    @SerialName("launched")
    val launched: String? = null,
    @SerialName("closed")
    val closed: String? = null,
    @SerialName("replaced_by")
    val replacedBy: String? = null,
    @SerialName("website")
    val website: String? = null,
    @SerialName("logo")
    val logo: String
)
