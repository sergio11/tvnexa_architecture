package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a request to create a user profile.
 *
 * @property alias The alias of the user profile.
 * @property pin The PIN associated with the profile (optional).
 * @property enableNSFW Indicates whether NSFW (Not Safe For Work) content is enabled for the profile.
 * @property avatarType The type of avatar associated with the profile.
 */
@Serializable
data class CreateProfileRequestDTO(

    @SerialName("alias")
    val alias: String,

    @SerialName("pin")
    val pin: Int?,

    @SerialName("enable_NSFW")
    val enableNSFW: Boolean,

    @SerialName("avatar_type")
    val avatarType: String
)