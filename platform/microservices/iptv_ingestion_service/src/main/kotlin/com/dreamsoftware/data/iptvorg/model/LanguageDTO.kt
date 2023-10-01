package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a language in an IPTV organization project.
 *
 * @property code ISO language code.
 * @property name Name of the language.
 */
@Serializable
data class LanguageDTO(
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String
)