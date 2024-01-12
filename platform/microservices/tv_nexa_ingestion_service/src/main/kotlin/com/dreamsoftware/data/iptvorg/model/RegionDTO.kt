package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a region in an IPTV organization project.
 *
 * @property code Code representing the region.
 * @property name Name of the region.
 * @property countries List of country names associated with the region.
 */
@Serializable
data class RegionDTO(
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String,
    @SerialName("countries")
    val countries: List<String>
)
