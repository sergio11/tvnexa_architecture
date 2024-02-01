package com.dreamsoftware.data.database.entity

import com.dreamsoftware.data.database.dao.ProfileType
import java.util.UUID


data class ProfileEntity(
    val uuid: UUID,
    val alias: String,
    val pin: Int,
    val userId: UUID,
    val isAdmin: Boolean,
    val type: ProfileType
)

data class CreateProfileEntity(
    val alias: String,
    val pin: Int,
    val userId: UUID,
    val isAdmin: Boolean,
    val type: ProfileType
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
    val isAdmin: Boolean?,
    val type: ProfileType?
)