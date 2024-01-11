package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IUserRepository
import com.dreamsoftware.api.utils.toJSON
import com.dreamsoftware.data.database.datasource.user.IUserDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Implementation of the UserRepository interface responsible for handling user-related operations.
 *
 * @property userDatabaseDataSource The data source responsible for user-related database operations.
 * @property cacheDatasource The cache data source for caching user information.
 */
internal class UserRepositoryImpl(
    private val userDatabaseDataSource: IUserDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), IUserRepository {

    /**
     * Prefix used for user cache keys.
     */
    private companion object {
        const val USER_CACHE_KEY_PREFIX = "users:"
    }

    /**
     * Checks if a user exists in the system based on the provided username and email.
     *
     * This method verifies the existence of a user by comparing both the username and email.
     *
     * @param username The username of the user to be checked.
     * @param email The email of the user to be checked.
     * @return Returns `true` if there is a user with the provided username and email; otherwise, returns `false`.
     */
    override suspend fun existsByUsernameOrEmail(username: String, email: String): Boolean = withContext(Dispatchers.IO) {
        userDatabaseDataSource.existsByUsernameOrEmail(username, email)
    }

    /**
     * Creates a new user based on the provided user data.
     *
     * @param user The user data to be created.
     */
    override suspend fun createUser(user: SaveUserEntity) = withContext(Dispatchers.IO) {
        userDatabaseDataSource.save(user)
    }

    /**
     * Retrieves a user by their ID from the cache or the database if not found in the cache.
     *
     * @param uuid The ID of the user to retrieve.
     * @return The UserEntity corresponding to the provided user ID, or null if not found.
     */
    override suspend fun getUserById(uuid: UUID): UserEntity? =
        retrieveFromCacheOrElse(cacheKey = USER_CACHE_KEY_PREFIX + uuid.toString()) {
            userDatabaseDataSource.findByKey(uuid)
        }

    /**
     * Retrieves a user by their username from the cache or the database if not found in the cache.
     *
     * @param username The username of the user to retrieve.
     * @return The UserEntity corresponding to the provided username, or null if not found.
     */
    override suspend fun getUserByUsername(username: String): UserEntity? =
        retrieveFromCacheOrElse(cacheKey = USER_CACHE_KEY_PREFIX + username) {
            userDatabaseDataSource.findByUsername(username)
        }

    /**
     * Attempts to sign in a user with the provided username and password. If successful, caches the user information.
     *
     * @param email The email of the user trying to sign in.
     * @param password The password of the user trying to sign in.
     * @return The UserEntity corresponding to the signed-in user, or null if the sign-in attempt fails.
     */
    override suspend fun signIn(email: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        userDatabaseDataSource.findByEmailAndPassword(email, password)?.also {
            runCatching {
                with(cacheDatasource) {
                    it.toJSON().let { userJSON ->
                        save(USER_CACHE_KEY_PREFIX + it.username, userJSON)
                        save(USER_CACHE_KEY_PREFIX + it.uuid.toString(), userJSON)
                    }
                }
            }
        }
    }
}