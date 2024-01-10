package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryResponseDTO(
    /**
     * The country code (e.g., "US" for United States).
     */
    @SerialName("code")
    val code: String,

    /**
     * The name of the country.
     */
    @SerialName("name")
    val name: String,

    /**
     * The URL to the flag image of the country (if available).
     */
    @SerialName("flag")
    val flag: String? = null,

    /**
     * The list of languages spoken in the country.
     */
    @SerialName("languages")
    val languages: List<LanguageResponseDTO>
)
