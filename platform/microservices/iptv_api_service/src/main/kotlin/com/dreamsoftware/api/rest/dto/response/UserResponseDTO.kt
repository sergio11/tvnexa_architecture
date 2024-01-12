package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    /**
     * The unique identifier of the user.
     */
    @SerialName("uuid")
    val uuid: String,

    /**
     * The username of the user.
     */
    @SerialName("username")
    val username: String,

    /**
     * The email address of the user.
     */
    @SerialName("email")
    val email: String,

    /**
     * The first name of the user.
     */
    @SerialName("firstName")
    val firstName: String,

    /**
     * The last name of the user.
     */
    @SerialName("lastName")
    val lastName: String
)