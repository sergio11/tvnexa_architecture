package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper implementation to convert ChannelEntityDAO objects to SimpleChannelEntity objects.
 */
class SimpleChannelEntityDaoMapper : ISimpleMapper<ChannelEntityDAO, SimpleChannelEntity> {

    /**
     * Maps a single ChannelEntityDAO object to a SimpleChannelEntity object.
     *
     * @param input The ChannelEntityDAO object to be mapped.
     * @return A mapped SimpleChannelEntity object.
     */
    override fun map(input: ChannelEntityDAO): SimpleChannelEntity = with(input) {
        SimpleChannelEntity(
            channelId = channelId,
            name = name,
            city = city,
            isNsfw = isNsfw,
            website = website,
            logo = logo
        )
    }

    /**
     * Maps a list of ChannelEntityDAO objects to a list of SimpleChannelEntity objects.
     *
     * @param input An Iterable collection of ChannelEntityDAO objects to be mapped.
     * @return An Iterable collection of mapped SimpleChannelEntity objects.
     */
    override fun mapList(input: Iterable<ChannelEntityDAO>): Iterable<SimpleChannelEntity> =
        input.map(::map)
}