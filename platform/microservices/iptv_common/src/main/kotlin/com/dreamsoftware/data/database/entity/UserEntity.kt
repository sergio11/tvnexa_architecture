package com.dreamsoftware.data.database.entity

import java.util.UUID


data class UserEntity(
    val uuid: UUID,
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class SaveUserEntity(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
