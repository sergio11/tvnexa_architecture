package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.response.CountryResponseDTO
import com.dreamsoftware.api.rest.dto.response.SubdivisionResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity

/**
 * `SubdivisionResponseDtoMapper` is a class responsible for mapping instances of `SubdivisionEntity`
 * to `SubdivisionResponseDTO` objects. It encapsulates the logic for transforming subdivision-related data
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
class SubdivisionResponseDtoMapper(
    private val countryMapper: ISimpleMapper<CountryEntity, CountryResponseDTO>
) : ISimpleMapper<SubdivisionEntity, SubdivisionResponseDTO> {

    /**
     * Maps a single `SubdivisionEntity` instance to a `SubdivisionResponseDTO`.
     *
     * @param input The `SubdivisionEntity` to be mapped.
     * @return A mapped `SubdivisionResponseDTO`.
     */
    override fun map(input: SubdivisionEntity): SubdivisionResponseDTO = SubdivisionResponseDTO(
        code = input.code,
        country = countryMapper.map(input.country),
        name = input.name
    )

    /**
     * Maps a list of `SubdivisionEntity` instances to a list of `SubdivisionResponseDTOs`.
     *
     * @param input The `Iterable` collection of `SubdivisionEntity` instances to be mapped.
     * @return A collection of mapped `SubdivisionResponseDTOs`.
     */
    override fun mapList(input: Iterable<SubdivisionEntity>): Iterable<SubdivisionResponseDTO> =
        input.map(::map)
}
