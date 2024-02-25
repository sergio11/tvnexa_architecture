package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a response containing user profile information.
 *
 * @property uuid The unique identifier of the profile.
 * @property alias The alias associated with the profile.
 * @property isAdmin Indicates whether the profile is an admin.
 * @property isSecured Indicates whether the profile has a secure PIN or not
 * @property enableNSFW A boolean indicating whether NSFW content is enabled for the profile.
 * @property avatarType The type of the profile avatar.
 */
@Serializable
data class ProfileResponseDTO(
    /**
     * The unique identifier of the profile.
     */
    @SerialName("uuid")
    val uuid: String,

    /**
     * The alias associated with the profile.
     */
    @SerialName("alias")
    val alias: String,

    /**
     * Indicates whether the profile is an admin.
     */
    @SerialName("is_admin")
    val isAdmin: Boolean,

    /**
     * Indicates whether the profile has a secure PIN or not
     */
    @SerialName("is_secured")
    val isSecured: Boolean,

    /**
     * A boolean indicating whether NSFW content is enabled for the profile.
     */
    @SerialName("enable_NSFW")
    val enableNSFW: Boolean,

    /**
     * The type of the profile avatar.
     */
    @SerialName("avatar_type")
    val avatarType: String
)