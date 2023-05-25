package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.ChannelStreamEntityDAO
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.ChannelStreamEntity

class ChannelStreamEntityDaoMapper(
    private val channelEntityDaoMapper: IMapper<ChannelEntityDAO, ChannelEntity>
): IMapper<ChannelStreamEntityDAO, ChannelStreamEntity> {

    override fun map(input: ChannelStreamEntityDAO): ChannelStreamEntity = with(input) {
        ChannelStreamEntity(
            url = url,
            channel = channelEntityDaoMapper.map(channel),
            httpReferrer = httpReferrer,
            userAgent = userAgent
        )
    }

    override fun mapList(input: Iterable<ChannelStreamEntityDAO>): Iterable<ChannelStreamEntity> =
        input.map(::map)
}