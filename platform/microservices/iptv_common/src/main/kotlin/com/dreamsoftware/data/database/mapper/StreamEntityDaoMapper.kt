package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.StreamEntityDAO
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.StreamEntity

class StreamEntityDaoMapper(
    private val channelEntityDaoMapper: IMapper<ChannelEntityDAO, ChannelEntity>
): IMapper<StreamEntityDAO, StreamEntity> {

    override fun map(input: StreamEntityDAO): StreamEntity = with(input) {
        StreamEntity(
            url = url,
            channel = channelEntityDaoMapper.map(channel),
            httpReferrer = httpReferrer,
            userAgent = userAgent
        )
    }

    override fun mapList(input: Iterable<StreamEntityDAO>): Iterable<StreamEntity> =
        input.map(::map)
}