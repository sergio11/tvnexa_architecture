package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.ChannelGuideEntityDAO
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.ChannelGuideEntity

class ChannelGuideEntityDaoSimpleMapper(
    private val channelMapper: ISimpleMapper<ChannelEntityDAO, ChannelEntity>
): ISimpleMapper<ChannelGuideEntityDAO, ChannelGuideEntity> {

    override fun map(input: ChannelGuideEntityDAO): ChannelGuideEntity = with(input) {
        ChannelGuideEntity(
            id = id.value,
            site = site,
            channel = channelMapper.map(channel),
            days = days,
            lang = lang
        )
    }

    override fun mapList(input: Iterable<ChannelGuideEntityDAO>): Iterable<ChannelGuideEntity> =
        input.map(::map)
}