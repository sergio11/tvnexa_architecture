package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO representing the request to create a new profile.
 *
 * @property alias The alias for the new profile.
 * @property pin The PIN for the new profile.
 * @property isAdmin Whether the new profile is an admin profile.
 * @property type The type of the new profile.
 */
@Serializable
data class CreateProfileRequestDTO(

    @SerialName("alias")
    val alias: String,

    @SerialName("pin")
    val pin: Int,

    @SerialName("is_admin")
    val isAdmin: Boolean,

    @SerialName("type")
    val type: String
)