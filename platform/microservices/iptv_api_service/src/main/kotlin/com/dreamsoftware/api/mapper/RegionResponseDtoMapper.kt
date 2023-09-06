package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.CountryResponseDTO
import com.dreamsoftware.api.dto.RegionResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.RegionEntity

class RegionResponseDtoMapper(
    private val countryMapper: IMapper<CountryEntity, CountryResponseDTO>
) : IMapper<RegionEntity, RegionResponseDTO> {

    override fun map(input: RegionEntity): RegionResponseDTO = RegionResponseDTO(
        code = input.code,
        name = input.name,
        countries = countryMapper.mapList(input.countries)
    )

    override fun mapList(input: Iterable<RegionEntity>): Iterable<RegionResponseDTO> =
        input.map(::map)
}
