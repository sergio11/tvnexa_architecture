package com.dreamsoftware.data.database.datasource.user.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.core.hash256
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.UserEntityDAO
import com.dreamsoftware.data.database.dao.UserTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.user.IUserDatabaseDataSource
import com.dreamsoftware.data.database.entity.CreateUserEntity
import com.dreamsoftware.data.database.entity.UpdateUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import java.util.UUID

/**
 * UserDatabaseDataSourceImpl: Implementation of IUserDatabaseDataSource for User entity with database operations.
 * @property database IDatabaseFactory instance.
 * @property mapper ISimpleMapper<UserEntityDAO, UserEntity> instance.
 */
internal class UserDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<UserEntityDAO, UserEntity>
) : SupportDatabaseDataSource<UUID, UserEntityDAO, CreateUserEntity, UserEntity>(
    database,
    mapper,
    UserEntityDAO
), IUserDatabaseDataSource {

    /**
     * Overrides the mapping of entity fields to database columns for saving/updating entities.
     * @param entityToSave SaveUserEntity object to map to the database columns.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: CreateUserEntity) = with(entityToSave) {
        this@onMapEntityToSave[UserTable.firstName] = firstName
        this@onMapEntityToSave[UserTable.lastName] = lastName
        this@onMapEntityToSave[UserTable.email] = email
        this@onMapEntityToSave[UserTable.username] = username
        this@onMapEntityToSave[UserTable.password] = password.hash256()
    }

    /**
     * Retrieves a user entity based on the provided username.
     *
     * @param username The username to search for.
     * @return The UserEntity corresponding to the provided username, or null if not found.
     */
    override suspend fun findByUsername(username: String): UserEntity? = execQuery {
        entityDAO.find { UserTable.username eq username }.singleOrNull()?.let(mapper::map)
    }

    /**
     * Retrieves a user entity based on the provided email and password.
     *
     * @param email The email to search for.
     * @param password The password associated with the username.
     * @return The UserEntity corresponding to the provided email and password, or null if not found.
     */
    override suspend fun findByEmailAndPassword(email: String, password: String): UserEntity? = execQuery {
        entityDAO.find { (UserTable.email eq email) and (UserTable.password eq password.hash256()) }.singleOrNull()?.let(mapper::map)
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
    override suspend fun existsByUsernameOrEmail(username: String, email: String): Boolean = execQuery {
        entityDAO.find { (UserTable.email eq email) or (UserTable.username eq username) }.count() > 0
    }

    /**
     * Asynchronously checks if a user with the specified [username] exists in the system.
     *
     * @param username The username to check for existence.
     * */
    override suspend fun existsByUsername(username: String): Boolean = execQuery {
        entityDAO.find { UserTable.username eq username }.count() > 0
    }

    /**
     * Checks whether a user with the specified ID exists.
     * @param userId The ID of the user to check for existence.
     * @return true if a user with the specified ID exists, false otherwise.
     */
    override suspend fun existsById(userId: UUID): Boolean = execQuery {
        entityDAO.find { UserTable.id eq userId }.count() > 0
    }

    /**
     * Asynchronously updates the profile of the user with the specified [userId] based on the provided [UpdateUserEntity].
     *
     * @param userId The unique identifier (UUID) of the user whose profile is to be updated.
     * @param data The [UpdateUserEntity] containing the updated profile information.
     */
    override suspend fun updateProfile(userId: UUID, data: UpdateUserEntity): Unit = execWrite {
        UserTable.update({ UserTable.id eq userId }) { statement ->
            with(data) {
                firstName?.let { statement[UserTable.firstName] = it }
                lastName?.let { statement[UserTable.lastName] = it }
                username?.let { statement[UserTable.username] = it }
            }
        }
    }
}