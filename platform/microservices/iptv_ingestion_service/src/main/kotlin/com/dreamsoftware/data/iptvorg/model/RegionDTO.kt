package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionDTO(
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String,
    @SerialName("countries")
    val countries: List<String>
)
