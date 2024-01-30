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
    val pin: Int = 123456,
    val userId: UUID,
    val isAdmin: Boolean = true,
    val type: ProfileType = ProfileType.BOY
)

data class UpdateProfileEntity(
    val alias: String?,
    val pin: Int?,
    val isAdmin: Boolean?,
    val type: ProfileType?
)