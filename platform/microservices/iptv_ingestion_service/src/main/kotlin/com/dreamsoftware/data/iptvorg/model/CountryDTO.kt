package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
