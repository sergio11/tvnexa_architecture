package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubdivisionDTO(
    @SerialName("country")
    val country: String,
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String
)
