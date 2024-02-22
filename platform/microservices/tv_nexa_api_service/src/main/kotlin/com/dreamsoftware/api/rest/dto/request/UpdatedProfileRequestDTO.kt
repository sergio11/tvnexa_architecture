package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to update an existing user profile.
 *
 * @property alias The updated alias for the profile, nullable.
 * @property pin The updated secret PIN for the profile, nullable.
 * @property enableNSFW The updated boolean indicating whether NSFW content is enabled for the profile, nullable.
 * @property avatarType The updated type of the profile, nullable.
 */
@Serializable
data class UpdatedProfileRequestDTO(

    @SerialName("alias")
    val alias: String? = null,

    @SerialName("pin")
    val pin: Int? = null,

    @SerialName("enable_NSFW")
    val enableNSFW: Boolean? = null,

    @SerialName("avatar_type")
    val avatarType: String? = null
)
