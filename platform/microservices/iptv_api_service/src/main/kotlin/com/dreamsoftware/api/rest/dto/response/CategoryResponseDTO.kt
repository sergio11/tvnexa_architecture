package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDTO(
    /**
     * The unique identifier of the category.
     */
    @SerialName("id")
    val id: String,

    /**
     * The name or title of the category.
     */
    @SerialName("name")
    val name: String
)
