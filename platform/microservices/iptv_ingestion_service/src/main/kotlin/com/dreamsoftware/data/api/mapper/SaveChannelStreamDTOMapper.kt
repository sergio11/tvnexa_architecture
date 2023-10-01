package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.iptvorg.model.ChannelStreamDTO

class SaveChannelStreamDTOMapper: ISimpleMapper<ChannelStreamDTO, SaveChannelStreamEntity> {
    override fun map(input: ChannelStreamDTO): SaveChannelStreamEntity = with(input) {
        SaveChannelStreamEntity(
            url = url,
            channelId = channel.orEmpty(),
            httpReferrer = httpReferrer,
            userAgent = userAgent
        )
    }
    override fun mapList(input: Iterable<ChannelStreamDTO>): Iterable<SaveChannelStreamEntity> =
        input.map(::map)
}