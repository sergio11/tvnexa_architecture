package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a subdivision in an IPTV organization project.
 *
 * @property country Country name to which the subdivision belongs.
 * @property code Code representing the subdivision.
 * @property name Name of the subdivision.
 */
@Serializable
data class SubdivisionDTO(
    @SerialName("country")
    val country: String,
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String
)
