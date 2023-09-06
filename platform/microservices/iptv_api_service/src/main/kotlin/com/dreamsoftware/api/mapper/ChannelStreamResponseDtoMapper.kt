package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.ChannelResponseDTO
import com.dreamsoftware.api.dto.ChannelStreamResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.ChannelStreamEntity

class ChannelStreamResponseDtoMapper(
    private val channelMapper: IMapper<ChannelEntity, ChannelResponseDTO>
) : IMapper<ChannelStreamEntity, ChannelStreamResponseDTO> {

    override fun map(input: ChannelStreamEntity): ChannelStreamResponseDTO = ChannelStreamResponseDTO(
        url = input.url,
        channel = channelMapper.map(input.channel),
        httpReferrer = input.httpReferrer,
        userAgent = input.userAgent
    )

    override fun mapList(input: Iterable<ChannelStreamEntity>): Iterable<ChannelStreamResponseDTO> =
        input.map(::map)
}
