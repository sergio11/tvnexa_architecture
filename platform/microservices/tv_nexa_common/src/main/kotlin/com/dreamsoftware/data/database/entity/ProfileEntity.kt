package com.dreamsoftware.data.database.entity

import com.dreamsoftware.data.database.dao.AvatarType
import java.util.UUID

data class ProfileEntity(
    val uuid: UUID,
    val alias: String,
    val pin: Int?,
    val userId: UUID,
    val isAdmin: Boolean,
    val avatarType: AvatarType,
    val enableNSFW: Boolean
)

data class CreateProfileEntity(
    val alias: String,
    val pin: Int?,
    val userId: UUID,
    val isAdmin: Boolean,
    val avatarType: AvatarType,
    val enableNSFW: Boolean
)

data class SaveFavoriteChannel(
    val profileId: UUID,
    val channelId: String
)

data class SaveBlockedChannel(
    val profileId: UUID,
    val channelId: String
)

data class UpdateProfileEntity(
    val alias: String?,
    val pin: Int?,
    val enableNSFW: Boolean?,
    val avatarType: AvatarType?
)