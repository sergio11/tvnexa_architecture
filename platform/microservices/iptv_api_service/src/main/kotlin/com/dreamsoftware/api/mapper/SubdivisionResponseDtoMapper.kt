package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.CountryResponseDTO
import com.dreamsoftware.api.dto.SubdivisionResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity

class SubdivisionResponseDtoMapper(
    private val countryMapper: IMapper<CountryEntity, CountryResponseDTO>
) : IMapper<SubdivisionEntity, SubdivisionResponseDTO> {

    override fun map(input: SubdivisionEntity): SubdivisionResponseDTO = SubdivisionResponseDTO(
        code = input.code,
        country = countryMapper.map(input.country),
        name = input.name
    )

    override fun mapList(input: Iterable<SubdivisionEntity>): Iterable<SubdivisionResponseDTO> =
        input.map(::map)
}
