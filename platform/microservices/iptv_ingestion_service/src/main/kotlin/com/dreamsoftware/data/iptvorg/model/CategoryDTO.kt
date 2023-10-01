package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a category (or genre) of content in an IPTV organization project.
 *
 * @property id Unique identifier for the category.
 * @property name Name or title of the category.
 */
@Serializable
data class CategoryDTO(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)
