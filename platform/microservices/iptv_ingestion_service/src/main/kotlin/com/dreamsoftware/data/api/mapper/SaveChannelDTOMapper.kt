package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.iptvorg.model.ChannelDTO

class SaveChannelDTOMapper: IMapper<ChannelDTO, SaveChannelEntity> {
    override fun map(input: ChannelDTO): SaveChannelEntity = with(input) {
        SaveChannelEntity(
            channelId = id,
            name = name,
            network = network,
            country = country,
            subdivision = subdivision,
            city = city,
            isNsfw = isNsfw,
            website = website,
            logo = logo,
            launched = launched,
            closed = closed,
            replacedBy = replacedBy,
            languages = languages,
            categories = categories
        )
    }
    override fun mapList(input: Iterable<ChannelDTO>): Iterable<SaveChannelEntity> =
        input.map(::map)
}