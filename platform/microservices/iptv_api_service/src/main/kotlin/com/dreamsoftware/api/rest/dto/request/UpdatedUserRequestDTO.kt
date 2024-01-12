package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing the request payload for updating user data.
 * Contains fields to update user profile information.
 */
@Serializable
data class UpdatedUserRequestDTO(
    /**
     * Updated first name of the user.
     */
    @SerialName("firstName")
    val firstName: String? = null,

    /**
     * Updated last name of the user.
     */
    @SerialName("lastName")
    val lastName: String? = null,

    /**
     * Updated username of the user.
     */
    @SerialName("username")
    val username: String? = null
)