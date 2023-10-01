package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity
import com.dreamsoftware.data.iptvorg.model.ChannelGuideDTO

class SaveChannelGuideDTOMapper: ISimpleMapper<ChannelGuideDTO, SaveChannelGuideEntity> {
    override fun map(input: ChannelGuideDTO): SaveChannelGuideEntity = with(input) {
        SaveChannelGuideEntity(
            site = site,
            channel = channel,
            lang = lang,
            days = days
        )
    }
    override fun mapList(input: Iterable<ChannelGuideDTO>): Iterable<SaveChannelGuideEntity> =
        input.map(::map)
}