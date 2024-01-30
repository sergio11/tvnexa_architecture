package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ProfileEntityDAO
import com.dreamsoftware.data.database.entity.ProfileEntity


class ProfileEntityDaoMapper : ISimpleMapper<ProfileEntityDAO, ProfileEntity> {

    override fun map(input: ProfileEntityDAO): ProfileEntity = with(input) {
        ProfileEntity(
            uuid = id.value,
            alias = alias,
            pin = pin,
            userId = userId,
            isAdmin = isAdmin,
            type = type
        )
    }

    override fun mapList(input: Iterable<ProfileEntityDAO>): Iterable<ProfileEntity> =
        input.map(::map)
}
