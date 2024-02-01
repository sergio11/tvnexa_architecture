package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.data.database.datasource.profiles.IBlockedChannelDataSource
import com.dreamsoftware.data.database.datasource.profiles.IFavoriteChannelDataSource
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.CreateProfileEntity
import com.dreamsoftware.data.database.entity.ProfileEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity
import com.dreamsoftware.data.database.entity.UpdateProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Implementation of the [IProfileRepository] interface responsible for handling profile-related operations.
 *
 * @property profileDatabaseDataSource The data source responsible for profile-related database operations.
 * @property blockedChannelsDataSource The data source for managing blocked channels related to profiles.
 * @property favoriteChannelDataSource The data source for managing favorite channels related to profiles.
 * @property cacheDatasource The cache data source for caching profile information.
 */
internal class ProfileRepositoryImpl(
    private val profileDatabaseDataSource: IProfileDatabaseDataSource,
    private val blockedChannelsDataSource: IBlockedChannelDataSource,
    private val favoriteChannelDataSource: IFavoriteChannelDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), IProfileRepository {

    /**
     * Retrieves the number of profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return The number of profiles associated with the specified user.
     */
    override suspend fun countByUser(uuid: UUID): Long = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.countByUser(uuid)
    }

    /**
     * Retrieves profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileEntity] objects representing the user's profiles.
     */
    override suspend fun findByUser(uuid: UUID): List<ProfileEntity> = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.findByUser(uuid)
    }

    /**
     * Updates a user's profile information.
     *
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param profile The updated information for the user's profile.
     */
    override suspend fun update(profileUuid: UUID, profile: UpdateProfileEntity) = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.updateProfile(profileUuid, profile)
    }

    /**
     * Retrieves a user's profile by its unique identifier.
     *
     * @param uuid The unique identifier of the profile.
     * @return A [ProfileEntity] object representing the user's profile, or null if not found.
     */
    override suspend fun getProfileById(uuid: UUID): ProfileEntity? = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.findByKey(uuid)
    }

    /**
     * Deletes a user's profile.
     *
     * @param uuid The unique identifier of the profile to be deleted.
     */
    override suspend fun deleteProfile(uuid: UUID) = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.deleteByKey(uuid)
    }

    /**
     * Creates a new profile for the authenticated user.
     *
     * @param data The [UpdateProfileEntity] containing the data for the new profile.
     * @return A [ProfileEntity] representing the newly created user profile.
     */
    override suspend fun createProfile(data: CreateProfileEntity): Unit = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.save(data)
    }

    /**
     * Verifies the PIN of a user's profile.
     *
     * @param profileUuid The unique identifier of the profile to be verified.
     * @param pin The PIN to be verified.
     * @return `true` if the PIN is verified successfully, `false` otherwise.
     */
    override suspend fun verifyPin(profileUuid: UUID, pin: Int): Boolean = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.verifyPin(profileUuid, pin)
    }

    /**
     * Suspended function to retrieve a list of blocked channels for the specified profile.
     *
     * @param profileUUID The unique identifier of the profile.
     * @return A list of [SimpleChannelEntity] representing the blocked channels for the specified profile.
     */
    override suspend fun getBlockedChannels(profileUUID: UUID): List<SimpleChannelEntity> = withContext(Dispatchers.IO) {
        blockedChannelsDataSource.findByProfile(profileUUID)
    }

    /**
     * Suspended function to retrieve a list of favorite channels for the specified profile.
     *
     * @param profileUUID The unique identifier of the profile.
     * @return A list of [SimpleChannelEntity] representing the favorite channels for the specified profile.
     */
    override suspend fun getFavoriteChannels(profileUUID: UUID): List<SimpleChannelEntity> = withContext(Dispatchers.IO) {
        favoriteChannelDataSource.findByProfile(profileUUID)
    }
}