package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.data.database.datasource.profiles.IBlockedChannelDataSource
import com.dreamsoftware.data.database.datasource.profiles.IFavoriteChannelDataSource
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.*
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
     * Checks whether the user has reached the limit of allowed profiles.
     * @param uuid The UUID of the user to check.
     * @return true if the user has reached the limit of allowed profiles, false otherwise.
     */
    override suspend fun hasUserProfileLimitReached(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.hasUserProfileLimitReached(uuid)
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

    /**
     * Saves a favorite channel for a given profile.
     * @param profileUUID The UUID of the profile for which the favorite channel will be saved.
     * @param channelId The ID of the channel to be saved as a favorite.
     * @return Unit
     * @throws Exception If an error occurs while saving the favorite channel.
     */
    override suspend fun saveFavoriteChannel(profileUUID: UUID, channelId: String) = withContext(Dispatchers.IO) {
        favoriteChannelDataSource.save(SaveFavoriteChannel(
            profileId = profileUUID,
            channelId = channelId,
        ))
    }

    /**
     * Checks if a channel is saved as a favorite for a user.
     *
     * @param profileUUID The UUID of the user's profile.
     * @param channelId The ID of the channel to be checked.
     * @return `true` if the channel is saved as a favorite, `false` otherwise.
     */
    override suspend fun isChannelSavedAsFavorite(profileUUID: UUID, channelId: String): Boolean = withContext(Dispatchers.IO) {
        favoriteChannelDataSource.isChannelSavedAsFavorite(profileUUID, channelId)
    }

    /**
     * Deletes a favorite channel from a user profile.
     * @param profileUUID The UUID of the user profile from which the favorite channel will be deleted.
     * @param channelId The ID of the channel to be deleted as a favorite.
     */
    override suspend fun deleteFavoriteChannel(profileUUID: UUID, channelId: String) = withContext(Dispatchers.IO) {
        favoriteChannelDataSource.deleteByProfileAndChannel(profileUUID, channelId)
    }

    /**
     * Saves a channel as blocked for a given user profile.
     * @param profileUUID The UUID of the profile for which the channel will be saved as blocked.
     * @param channelId The ID of the channel to be saved as blocked.
     */
    override suspend fun saveBlockedChannel(profileUUID: UUID, channelId: String) = withContext(Dispatchers.IO) {
        blockedChannelsDataSource.save(
            SaveBlockedChannel(
                profileId = profileUUID,
                channelId = channelId,
            )
        )
    }

    /**
     * Deletes a blocked channel from a user profile.
     * @param profileUUID The UUID of the profile from which the blocked channel will be deleted.
     * @param channelId The ID of the channel to be deleted from the blocked list.
     */
    override suspend fun deleteBlockedChannel(profileUUID: UUID, channelId: String) = withContext(Dispatchers.IO) {
        blockedChannelsDataSource.deleteByProfileAndChannel(profileId = profileUUID, channelId = channelId)
    }

    /**
     * Checks whether a profile with the specified ID exists.
     * @param profileId The UUID of the profile to check for existence.
     * @return true if a profile with the specified ID exists, false otherwise.
     */
    override suspend fun existsById(profileId: UUID): Boolean = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.existsById(profileId)
    }

    /**
     * Checks whether a profile with the specified ID can be managed by the user with the provided ID.
     * @param profileId The UUID of the profile to check for manageability.
     * @param userId The UUID of the user attempting to manage the profile.
     * @return true if the user can manage the profile, false otherwise.
     */
    override suspend fun canBeManagedByUser(profileId: UUID, userId: UUID): Boolean = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.canBeManagedByUser(profileId, userId)
    }
}