package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.ProfileEntity
import com.dreamsoftware.data.database.entity.UpdateProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Implementation of the [IProfileRepository] interface responsible for handling profile-related operations.
 *
 * @property profileDatabaseDataSource The data source responsible for profile-related database operations.
 * @property cacheDatasource The cache data source for caching profile information.
 */
internal class ProfileRepositoryImpl(
    private val profileDatabaseDataSource: IProfileDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), IProfileRepository {

    /**
     * Retrieves the number of profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return The number of profiles associated with the specified user.
     */
    override suspend fun countByUser(uuid: String): Long = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.countByUser(uuid)
    }

    /**
     * Retrieves profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileEntity] objects representing the user's profiles.
     */
    override suspend fun findByUser(uuid: String): List<ProfileEntity> = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.findByUser(uuid)
    }

    /**
     * Updates a user's profile information.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param profile The updated information for the user's profile.
     */
    override suspend fun update(userUuid: String, profileUuid: String, profile: UpdateProfileEntity) = withContext(Dispatchers.IO) {
        profileDatabaseDataSource.updateProfile(userUuid, profileUuid, profile)
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
}