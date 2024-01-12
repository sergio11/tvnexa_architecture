package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a country in an IPTV organization project.
 *
 * @property name Name of the country.
 * @property code ISO country code.
 * @property languages List of languages spoken in the country.
 * @property flag URL to the flag image representing the country.
 */
@Serializable
data class CountryDTO(
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String,
    @SerialName("languages")
    val languages: List<String>,
    @SerialName("flag")
    val flag: String
)
