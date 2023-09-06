package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.LanguageResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.LanguageEntity

class LanguageResponseDtoMapper : IMapper<LanguageEntity, LanguageResponseDTO> {

    override fun map(input: LanguageEntity): LanguageResponseDTO = LanguageResponseDTO(
        code = input.code,
        name = input.name
    )

    override fun mapList(input: Iterable<LanguageEntity>): Iterable<LanguageResponseDTO> =
        input.map(::map)
}
