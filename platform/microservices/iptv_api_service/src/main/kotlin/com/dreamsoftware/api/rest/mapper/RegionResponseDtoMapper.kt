package com.dreamsoftware.api.rest.mapper

/**
 * `RegionResponseDtoMapper` is a class responsible for mapping instances of `RegionEntity`
 * to `RegionResponseDTO` objects. It encapsulates the logic for transforming region-related data
 * from the data layer to DTOs for use in the API layer.
 *
 * This mapper implements the `ISimpleMapper` interface and provides methods for mapping single entities
 * and lists of entities from the data layer to DTOs in the API layer.
 *
 * @param countryMapper A mapper for converting `CountryEntity` instances to `CountryResponseDTO`.
 *
 * @author Sergio Sánchez Sánchez
 * @version 1.0
 */

import com.dreamsoftware.api.rest.dto.CountryResponseDTO
import com.dreamsoftware.api.rest.dto.RegionResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.RegionEntity

class RegionResponseDtoMapper(
    private val countryMapper: ISimpleMapper<CountryEntity, CountryResponseDTO>
) : ISimpleMapper<RegionEntity, RegionResponseDTO> {

    /**
     * Maps a single `RegionEntity` instance to a `RegionResponseDTO`.
     *
     * @param input The `RegionEntity` to be mapped.
     * @return A mapped `RegionResponseDTO`.
     */
    override fun map(input: RegionEntity): RegionResponseDTO = RegionResponseDTO(
        code = input.code,
        name = input.name,
        countries = countryMapper.mapList(input.countries)
    )

    /**
     * Maps a list of `RegionEntity` instances to a list of `RegionResponseDTOs`.
     *
     * @param input The `Iterable` collection of `RegionEntity` instances to be mapped.
     * @return A collection of mapped `RegionResponseDTOs`.
     */
    override fun mapList(input: Iterable<RegionEntity>): Iterable<RegionResponseDTO> =
        input.map(::map)
}
