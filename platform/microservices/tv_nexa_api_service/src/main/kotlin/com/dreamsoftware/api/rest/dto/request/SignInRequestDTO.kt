package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing the request payload for user sign-in.
 * Contains information required for user authentication.
 */
@Serializable
data class SignInRequestDTO(
    /**
     * email used for user authentication.
     */
    @SerialName("email")
    val email: String,

    /**
     * Password for user authentication.
     */
    @SerialName("password")
    val password: String
)
