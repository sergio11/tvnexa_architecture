package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
