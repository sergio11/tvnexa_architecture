package com.dreamsoftware.data.database.datasource.user.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.UserEntityDAO
import com.dreamsoftware.data.database.dao.UserTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.user.IUserDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * UserDatabaseDataSourceImpl: Implementation of IUserDatabaseDataSource for User entity with database operations.
 * @property database IDatabaseFactory instance.
 * @property mapper ISimpleMapper<UserEntityDAO, UserEntity> instance.
 */
internal class UserDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<UserEntityDAO, UserEntity>
) : SupportDatabaseDataSource<Long, UserEntityDAO, SaveUserEntity, UserEntity>(
    database,
    mapper,
    UserEntityDAO
), IUserDatabaseDataSource {

    /**
     * Overrides the mapping of entity fields to database columns for saving/updating entities.
     * @param entityToSave SaveUserEntity object to map to the database columns.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveUserEntity) = with(entityToSave) {
        this@onMapEntityToSave[UserTable.firstName] = firstName
        this@onMapEntityToSave[UserTable.lastName] = lastName
        this@onMapEntityToSave[UserTable.email] = email
        this@onMapEntityToSave[UserTable.username] = username
        this@onMapEntityToSave[UserTable.password] = password
    }
}