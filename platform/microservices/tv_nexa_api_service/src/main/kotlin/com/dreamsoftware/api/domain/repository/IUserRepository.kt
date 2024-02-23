package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.CreateUserEntity
import com.dreamsoftware.data.database.entity.UpdateUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import java.util.UUID

/**
 * Interface for repository operations related to user authentication.
 */
interface IUserRepository {

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

    /**
     * Asynchronously checks if a user with the specified [username] exists in the system.
     *
     * @param username The username to check for existence.
     *
     */
    suspend fun existsByUsername(username: String): Boolean

    /**
     * Checks whether a user with the specified ID exists.
     * @param userId The ID of the user to check for existence.
     * @return true if a user with the specified ID exists, false otherwise.
     */
    suspend fun existsById(userId: UUID): Boolean

    /**
     * Creates a new user in the system.
     *
     * @param user User information to create.
     */
    suspend fun createUser(user: CreateUserEntity)

    /**
     * Asynchronously updates a user based on the provided [UpdateUserEntity].
     *
     * @param uuid ID of the user to fetch.
     * @param user The [UpdateUserEntity] containing the updated user information.
     */
    suspend fun updateUser(uuid: UUID, user: UpdateUserEntity)

    /**
     * Gets a user based on their ID.
     *
     * @param uuid ID of the user to fetch.
     * @return Returns the user entity corresponding to the provided ID, or null if not found.
     */
    suspend fun getUserById(uuid: UUID): UserEntity?

    /**
     * Gets a user based on their username.
     *
     * @param username Username of the user to fetch.
     * @return Returns the user entity corresponding to the provided username, or null if not found.
     */
    suspend fun getUserByUsername(username: String): UserEntity?

    /**
     * Attempts to authenticate a user using their email and password.
     *
     * @param email User's email.
     * @param password Password provided by the user.
     * @return Returns a user entity if authentication is successful, otherwise returns null.
     */
    suspend fun signIn(email: String, password: String): UserEntity?
}