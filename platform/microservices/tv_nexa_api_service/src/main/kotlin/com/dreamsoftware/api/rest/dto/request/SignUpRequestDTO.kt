package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing the request payload for user sign-up.
 * Contains information required for user registration.
 */
@Serializable
data class SignUpRequestDTO(
    /**
     * Username chosen by the user for registration.
     */
    @SerialName("username")
    val username: String,

    /**
     * Password chosen by the user for registration.
     */
    @SerialName("password")
    val password: String,

    /**
     * Email address provided by the user for registration.
     */
    @SerialName("email")
    val email: String,

    /**
     * First name of the user for registration.
     */
    @SerialName("firstName")
    val firstName: String,

    /**
     * Last name of the user for registration.
     */
    @SerialName("lastName")
    val lastName: String
)