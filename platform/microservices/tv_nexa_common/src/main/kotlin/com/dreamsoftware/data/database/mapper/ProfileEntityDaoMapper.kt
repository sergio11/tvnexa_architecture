package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ProfileEntityDAO
import com.dreamsoftware.data.database.entity.ProfileEntity

/**
 * Mapper class for mapping ProfileEntityDAO objects to ProfileEntity objects.
 * Implements ISimpleMapper interface.
 */
class ProfileEntityDaoMapper : ISimpleMapper<ProfileEntityDAO, ProfileEntity> {

    /**
     * Maps a single ProfileEntityDAO object to a ProfileEntity object.
     *
     * @param input The ProfileEntityDAO object to map.
     * @return The mapped ProfileEntity object.
     */
    override fun map(input: ProfileEntityDAO): ProfileEntity = with(input) {
        ProfileEntity(
            uuid = id.value,
            alias = alias,
            pin = pin,
            userId = userId,
            isAdmin = isAdmin,
            avatarType = avatarType,
            enableNSFW = enableNSFW
        )
    }

    /**
     * Maps a collection of ProfileEntityDAO objects to a collection of ProfileEntity objects.
     *
     * @param input The collection of ProfileEntityDAO objects to map.
     * @return The mapped collection of ProfileEntity objects.
     */
    override fun mapList(input: Iterable<ProfileEntityDAO>): Iterable<ProfileEntity> =
        input.map(::map)
}