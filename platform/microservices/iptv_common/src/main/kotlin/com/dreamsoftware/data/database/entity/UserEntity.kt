package com.dreamsoftware.data.database.entity


data class UserEntity(
    val id: Long,
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
