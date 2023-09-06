package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.CategoryResponseDTO
import com.dreamsoftware.api.dto.ChannelResponseDTO
import com.dreamsoftware.api.dto.EpgChannelProgrammeResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity

class EpgChannelProgrammeResponseDtoMapper(
    private val channelMapper: IMapper<ChannelEntity, ChannelResponseDTO>,
    private val categoryMapper: IMapper<CategoryEntity, CategoryResponseDTO>
) : IMapper<EpgChannelProgrammeEntity, EpgChannelProgrammeResponseDTO> {

    override fun map(input: EpgChannelProgrammeEntity): EpgChannelProgrammeResponseDTO = EpgChannelProgrammeResponseDTO(
        id = input.id,
        title = input.title,
        channel = channelMapper.map(input.channel),
        category = input.category?.let { categoryMapper.map(it) },
        start = input.start.toString(),
        end = input.end.toString(),
        date = input.date.toString()
    )

    override fun mapList(input: Iterable<EpgChannelProgrammeEntity>): Iterable<EpgChannelProgrammeResponseDTO> {
        return input.map(::map)
    }
}
