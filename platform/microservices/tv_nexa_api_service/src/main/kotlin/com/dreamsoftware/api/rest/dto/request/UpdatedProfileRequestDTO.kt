package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing the data sent in a request to update a user profile.
 *
 * @property alias The alias associated with the user's profile.
 * @property pin The Personal Identification Number (PIN) associated with the user's profile.
 * @property isAdmin A flag indicating whether the user has administrative privileges.
 * @property type The type of the user's profile.
 */
@Serializable
data class UpdatedProfileRequestDTO(

    @SerialName("alias")
    val alias: String? = null,

    @SerialName("pin")
    val pin: Int? = null,

    @SerialName("is_admin")
    val isAdmin: Boolean? = null,

    @SerialName("type")
    val type: String? = null
)