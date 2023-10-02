package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.ChannelStreamEntityDAO
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.ChannelStreamEntity

/**
 * Mapper for converting [ChannelStreamEntityDAO] objects to [ChannelStreamEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([ChannelStreamEntityDAO])
 * to the domain representation ([ChannelStreamEntity]) of a channel stream.
 *
 * @param channelEntityDaoMapper A mapper for converting [ChannelEntityDAO] to [ChannelEntity].
 */
class ChannelStreamEntityDaoSimpleMapper(
    private val channelEntityDaoMapper: ISimpleMapper<ChannelEntityDAO, ChannelEntity>
) : ISimpleMapper<ChannelStreamEntityDAO, ChannelStreamEntity> {

    /**
     * Maps a single [ChannelStreamEntityDAO] object to a [ChannelStreamEntity] object.
     *
     * @param input The input [ChannelStreamEntityDAO] to map.
     * @return The mapped [ChannelStreamEntity].
     */
    override fun map(input: ChannelStreamEntityDAO): ChannelStreamEntity = with(input) {
        ChannelStreamEntity(
            url = url,
            channel = channelEntityDaoMapper.map(channel),
            httpReferrer = httpReferrer,
            userAgent = userAgent
        )
    }

    /**
     * Maps a list of [ChannelStreamEntityDAO] objects to a list of [ChannelStreamEntity] objects.
     *
     * @param input The input list of [ChannelStreamEntityDAO] objects to map.
     * @return The mapped list of [ChannelStreamEntity] objects.
     */
    override fun mapList(input: Iterable<ChannelStreamEntityDAO>): Iterable<ChannelStreamEntity> =
        input.map(::map)
}