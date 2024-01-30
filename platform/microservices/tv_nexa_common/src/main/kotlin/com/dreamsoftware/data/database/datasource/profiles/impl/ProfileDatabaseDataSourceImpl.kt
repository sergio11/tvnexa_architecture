package com.dreamsoftware.data.database.datasource.profiles.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ProfileEntityDAO
import com.dreamsoftware.data.database.dao.ProfileTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.profiles.IProfileDatabaseDataSource
import com.dreamsoftware.data.database.entity.CreateProfileEntity
import com.dreamsoftware.data.database.entity.ProfileEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.*

internal class ProfileDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<ProfileEntityDAO, ProfileEntity>
) : SupportDatabaseDataSource<UUID, ProfileEntityDAO, CreateProfileEntity, ProfileEntity>(
    database,
    mapper,
    ProfileEntityDAO
), IProfileDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: CreateProfileEntity) = with(entityToSave) {
        this@onMapEntityToSave[ProfileTable.alias] = alias
        this@onMapEntityToSave[ProfileTable.isAdmin] = isAdmin
        this@onMapEntityToSave[ProfileTable.pin] = pin
        this@onMapEntityToSave[ProfileTable.type] = type
        this@onMapEntityToSave[ProfileTable.userId] = userId
    }
}