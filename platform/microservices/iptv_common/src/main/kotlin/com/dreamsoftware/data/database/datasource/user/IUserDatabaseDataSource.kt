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

    /**
     * Checks if a user exists in the system based on the provided username and email.
     *
     * This method verifies the existence of a user by comparing both the username and email.
     *
     * @param username The username of the user to be checked.
     * @param email The email of the user to be checked.
     * @return Returns `true` if there is a user with the provided username and email; otherwise, returns `false`.
     */
    suspend fun existsByUsernameOrEmail(username: String, email: String): Boolean
}