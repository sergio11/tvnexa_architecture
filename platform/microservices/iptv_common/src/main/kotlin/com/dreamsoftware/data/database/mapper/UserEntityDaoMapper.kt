package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.UserEntityDAO
import com.dreamsoftware.data.database.entity.UserEntity

/**
 * UserEntityDaoMapper: Maps UserEntityDAO to UserEntity and vice versa.
 * Implements ISimpleMapper interface.
 */
class UserEntityDaoMapper : ISimpleMapper<UserEntityDAO, UserEntity> {

    /**
     * Maps a single UserEntityDAO to a UserEntity.
     * @param input Input UserEntityDAO object to map.
     * @return Mapped UserEntity object.
     */
    override fun map(input: UserEntityDAO): UserEntity = with(input) {
        UserEntity(
            id = id.value,
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }

    /**
     * Maps a list of UserEntityDAO objects to a list of UserEntity objects.
     * @param input Input iterable of UserEntityDAO objects to map.
     * @return Mapped iterable of UserEntity objects.
     */
    override fun mapList(input: Iterable<UserEntityDAO>): Iterable<UserEntity> =
        input.map(::map)
}
