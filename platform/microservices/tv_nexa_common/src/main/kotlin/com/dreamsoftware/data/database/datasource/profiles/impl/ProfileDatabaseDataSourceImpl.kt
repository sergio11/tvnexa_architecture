package com.dreamsoftware.data.database.datasource.profiles.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.core.toUUID
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ProfileEntityDAO
import com.dreamsoftware.data.database.dao.ProfileTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.CreateProfileEntity
import com.dreamsoftware.data.database.entity.ProfileEntity
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
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
        this@onMapEntityToSave[ProfileTable.type] = type
        this@onMapEntityToSave[ProfileTable.userId] = userId
    }

    /**
     * Counts the number of profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return The number of profiles associated with the specified user.
     */
    override suspend fun countByUser(uuid: String): Long = execQuery {
        ProfileTable
            .slice(ProfileTable.userId.count())
            .select { ProfileTable.userId eq uuid.toUUID() }
            .single()[ProfileTable.userId.count()]
    }
}
