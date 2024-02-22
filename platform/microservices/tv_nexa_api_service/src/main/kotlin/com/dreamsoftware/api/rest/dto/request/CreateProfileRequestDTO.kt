package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to create a new user profile.
 *
 * @property alias The alias for the new profile.
 * @property pin The secret PIN for the new profile.
 * @property enableNSFW A boolean indicating whether NSFW content is enabled for the new profile.
 * @property type The type of the new profile.
 */
@Serializable
data class CreateProfileRequestDTO(

    @SerialName("alias")
    val alias: String,

    @SerialName("pin")
    val pin: Int,

    @SerialName("enable_NSFW")
    val enableNSFW: Boolean,

    @SerialName("type")
    val type: String
)