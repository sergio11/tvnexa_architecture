package com.dreamsoftware.api.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageResponseDTO(
    /**
     * The language code (e.g., "en" for English).
     */
    @SerialName("code")
    val code: String,

    /**
     * The name of the language.
     */
    @SerialName("name")
    val name: String
)
