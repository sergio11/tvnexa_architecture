package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.SaveStreamEntity
import com.dreamsoftware.data.iptvorg.model.StreamDTO

class SaveStreamDTOMapper: IMapper<StreamDTO, SaveStreamEntity> {
    override fun map(input: StreamDTO): SaveStreamEntity = with(input) {
        SaveStreamEntity(
            url = url,
            channelId = channel,
            httpReferrer = httpReferrer,
            userAgent = userAgent
        )
    }
    override fun mapList(input: Iterable<StreamDTO>): Iterable<SaveStreamEntity> =
        input.map(::map)
}