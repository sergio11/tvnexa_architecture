package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.ChannelGuideResponseDTO
import com.dreamsoftware.api.dto.ChannelResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.ChannelGuideEntity

class ChannelGuideResponseDtoMapper(private val channelMapper: IMapper<ChannelEntity, ChannelResponseDTO>) :
    IMapper<ChannelGuideEntity, ChannelGuideResponseDTO> {

    override fun map(input: ChannelGuideEntity): ChannelGuideResponseDTO = ChannelGuideResponseDTO(
        id = input.id,
        site = input.site,
        channel = channelMapper.map(input.channel),
        lang = input.lang,
        days = input.days
    )

    override fun mapList(input: Iterable<ChannelGuideEntity>): Iterable<ChannelGuideResponseDTO> =
        input.map(::map)
}
