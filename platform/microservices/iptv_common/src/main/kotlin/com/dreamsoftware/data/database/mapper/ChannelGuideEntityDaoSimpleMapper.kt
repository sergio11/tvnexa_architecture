package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.ChannelGuideEntityDAO
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.ChannelGuideEntity

/**
 * Mapper for converting [ChannelGuideEntityDAO] objects to [ChannelGuideEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([ChannelGuideEntityDAO])
 * to the domain representation ([ChannelGuideEntity]) of a channel guide.
 *
 * @param channelMapper A mapper for converting [ChannelEntityDAO] to [ChannelEntity].
 */
class ChannelGuideEntityDaoSimpleMapper(
    private val channelMapper: ISimpleMapper<ChannelEntityDAO, ChannelEntity>
) : ISimpleMapper<ChannelGuideEntityDAO, ChannelGuideEntity> {

    /**
     * Maps a single [ChannelGuideEntityDAO] object to a [ChannelGuideEntity] object.
     *
     * @param input The input [ChannelGuideEntityDAO] to map.
     * @return The mapped [ChannelGuideEntity].
     */
    override fun map(input: ChannelGuideEntityDAO): ChannelGuideEntity = with(input) {
        ChannelGuideEntity(
            id = id.value,
            site = site,
            channel = channelMapper.map(channel),
            days = days,
            lang = lang
        )
    }

    /**
     * Maps a list of [ChannelGuideEntityDAO] objects to a list of [ChannelGuideEntity] objects.
     *
     * @param input The input list of [ChannelGuideEntityDAO] objects to map.
     * @return The mapped list of [ChannelGuideEntity] objects.
     */
    override fun mapList(input: Iterable<ChannelGuideEntityDAO>): Iterable<ChannelGuideEntity> =
        input.map(::map)
}