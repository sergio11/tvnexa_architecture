package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}