package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing the response data after successful authentication.
 * Contains a token and user information.
 */
@Serializable
data class AuthResponseDTO(
    /**
     * Authentication token generated upon successful authentication.
     */
    @SerialName("token")
    val token: String,

    /**
     * User information associated with the authenticated user.
     */
    @SerialName("user")
    val user: UserResponseDTO,

    /**
     * The number of profiles associated with the authenticated user.
     */
    @SerialName("profiles_count")
    val profilesCount: Long
)