package com.dreamsoftware.data.database.datasource.user

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import java.util.*

/**
 * Interface representing a user database data source.
 * Extends ISupportDatabaseDataSource with types Long, SaveUserEntity, and UserEntity.
 */
interface IUserDatabaseDataSource : ISupportDatabaseDataSource<UUID, SaveUserEntity, UserEntity> {

    /**
     * Retrieves a user entity based on the provided username.
     *
     * @param username The username to search for.
     * @return The UserEntity corresponding to the provided username, or null if not found.
     */
    suspend fun findByUsername(username: String): UserEntity?

    /**
     * Retrieves a user entity based on the provided email and password.
     *
     * @param email The email to search for.
     * @param password The password associated with the username.
     * @return The UserEntity corresponding to the provided email and password, or null if not found.
     */
    suspend fun findByEmailAndPassword(email: String, password: String): UserEntity?
}