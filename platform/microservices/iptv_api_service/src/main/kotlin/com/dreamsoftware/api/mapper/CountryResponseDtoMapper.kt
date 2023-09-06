package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.CountryResponseDTO
import com.dreamsoftware.api.dto.LanguageResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity

class CountryResponseDtoMapper(
    private val languageMapper: IMapper<LanguageEntity, LanguageResponseDTO>
) : IMapper<CountryEntity, CountryResponseDTO> {

    override fun map(input: CountryEntity): CountryResponseDTO = CountryResponseDTO(
        code = input.code,
        name = input.name,
        flag = input.flag,
        languages = languageMapper.mapList(input.languages)
    )

    override fun mapList(input: Iterable<CountryEntity>): Iterable<CountryResponseDTO> {
        return input.map(::map)
    }
}
