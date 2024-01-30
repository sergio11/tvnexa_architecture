package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the response for profile information.
 *
 * @property uuid The unique identifier of the profile.
 * @property alias The alias associated with the profile.
 * @property isAdmin Indicates whether the profile is an admin.
 * @property type The type of the profile.
 */
@Serializable
data class ProfileResponseDTO(
    /**
     * The unique identifier of the profile.
     */
    @SerialName("uuid")
    val uuid: String,

    /**
     * The alias associated with the profile.
     */
    @SerialName("alias")
    val alias: String,

    /**
     * Indicates whether the profile is an admin.
     */
    @SerialName("is_admin")
    val isAdmin: Boolean,

    /**
     * The type of the profile.
     */
    @SerialName("type")
    val type: String
)