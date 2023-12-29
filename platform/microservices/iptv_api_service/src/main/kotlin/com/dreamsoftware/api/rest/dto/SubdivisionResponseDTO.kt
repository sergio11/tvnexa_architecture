package com.dreamsoftware.api.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubdivisionResponseDTO(
    /**
     * The code or identifier of the subdivision.
     */
    @SerialName("code")
    val code: String,

    /**
     * The country to which the subdivision belongs.
     */
    @SerialName("country")
    val country: CountryResponseDTO,

    /**
     * The name or title of the subdivision.
     */
    @SerialName("name")
    val name: String
)