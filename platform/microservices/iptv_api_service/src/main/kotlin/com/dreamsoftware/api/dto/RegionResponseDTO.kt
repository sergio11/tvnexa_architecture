package com.dreamsoftware.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionResponseDTO(
    /**
     * The code or identifier of the region.
     */
    @SerialName("code")
    val code: String,

    /**
     * The name or title of the region.
     */
    @SerialName("name")
    val name: String,

    /**
     * The list of countries within the region.
     */
    @SerialName("countries")
    val countries: Iterable<CountryResponseDTO>
)