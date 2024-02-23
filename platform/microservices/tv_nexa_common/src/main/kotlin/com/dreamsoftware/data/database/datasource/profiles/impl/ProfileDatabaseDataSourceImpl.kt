package com.dreamsoftware.data.database.datasource.profiles.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ProfileEntityDAO
import com.dreamsoftware.data.database.dao.ProfileTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.CreateProfileEntity
import com.dreamsoftware.data.database.entity.ProfileEntity
import com.dreamsoftware.data.database.entity.UpdateProfileEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import java.util.*

/**
 * Implementation of the [IProfileDatabaseDataSource] interface responsible for profile-related database operations.
 *
 * @param database The [IDatabaseFactory] instance for database operations.
 * @param mapper The mapper to convert between database entities and domain entities.
 */
internal class ProfileDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<ProfileEntityDAO, ProfileEntity>
) : SupportDatabaseDataSource<UUID, ProfileEntityDAO, CreateProfileEntity, ProfileEntity>(
    database,
    mapper,
    ProfileEntityDAO
), IProfileDatabaseDataSource {

    /**
     * Maps properties from [CreateProfileEntity] to an Exposed [UpdateBuilder].
     *
     * @param entityToSave The [CreateProfileEntity] instance to map to the [UpdateBuilder].
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: CreateProfileEntity) = with(entityToSave) {
        this@onMapEntityToSave[ProfileTable.alias] = alias
        this@onMapEntityToSave[ProfileTable.isAdmin] = isAdmin
        this@onMapEntityToSave[ProfileTable.pin] = pin
        this@onMapEntityToSave[ProfileTable.avatar_type] = avatarType
        this@onMapEntityToSave[ProfileTable.userId] = userId
        this@onMapEntityToSave[ProfileTable.enableNSFW] = enableNSFW
    }

    /**
     * Counts the number of profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return The number of profiles associated with the specified user.
     */
    override suspend fun countByUser(uuid: UUID): Long = execQuery {
        ProfileTable
            .slice(ProfileTable.userId.count())
            .select { ProfileTable.userId eq uuid }
            .single()[ProfileTable.userId.count()]
    }

    /**
     * Retrieves profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileEntity] objects representing the user's profiles.
     */
    override suspend fun findByUser(uuid: UUID): List<ProfileEntity> = execQuery {
        entityDAO.find { ProfileTable.userId eq uuid }.map(mapper::map)
    }

    /**
     * update a user's profile information.
     *
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param data The [UpdateProfileEntity] containing the updated profile information.
     */
    override suspend fun updateProfile(profileUuid: UUID, data: UpdateProfileEntity): Unit = execWrite {
        ProfileTable.update({ ProfileTable.id eq profileUuid }) { statement ->
            with(data) {
                alias?.let { statement[ProfileTable.alias] = it }
                pin?.let { statement[ProfileTable.pin] = it }
                enableNSFW?.let { statement[ProfileTable.enableNSFW] = enableNSFW }
                avatarType?.let { statement[avatar_type] = it }
            }
        }
    }

    /**
     * Suspended function to verify the PIN of a user's profile.
     *
     * @param profileUuid The unique identifier of the profile to be verified.
     * @param pin The PIN to be verified.
     * @return `true` if the PIN is verified successfully, `false` otherwise.
     */
    override suspend fun verifyPin(profileUuid: UUID, pin: Int): Boolean = execQuery {
        entityDAO.find { ProfileTable.id eq profileUuid and(ProfileTable.pin eq pin) }.count() > 0
    }

    /**
     * Checks whether a profile with the specified ID exists.
     * @param profileId The UUID of the profile to check for existence.
     * @return true if a profile with the specified ID exists, false otherwise.
     */
    override suspend fun existsById(profileId: UUID): Boolean = execQuery {
        entityDAO.find { ProfileTable.id eq profileId }.count() > 0
    }

    /**
     * Checks whether a profile with the specified ID can be managed by the user with the provided ID.
     * @param profileId The UUID of the profile to check for manageability.
     * @param userId The UUID of the user attempting to manage the profile.
     * @return true if the user can manage the profile, false otherwise.
     */
    override suspend fun canBeManagedByUser(profileId: UUID, userId: UUID): Boolean = execQuery {
        entityDAO.find { ProfileTable.id eq profileId and(ProfileTable.userId eq userId) }.count() > 0
    }
}
