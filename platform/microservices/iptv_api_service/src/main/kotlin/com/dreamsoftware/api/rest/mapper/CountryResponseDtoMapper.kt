package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.CountryResponseDTO
import com.dreamsoftware.api.rest.dto.LanguageResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity

/**
 * `CountryResponseDtoMapper` is a class responsible for mapping instances of `CountryEntity`
 * to `CountryResponseDTO` objects. It encapsulates the logic for transforming country-related data
 * from the data layer to DTOs for use in the API layer.
 *
 * This mapper implements the `ISimpleMapper` interface and provides methods for mapping single entities
 * and lists of entities from the data layer to DTOs in the API layer.
 *
 * @param languageMapper A mapper for converting `LanguageEntity` instances to `LanguageResponseDTO`.
 *
 * @author Sergio Sánchez Sánchez
 * @version 1.0
 */
class CountryResponseDtoMapper(
    private val languageMapper: ISimpleMapper<LanguageEntity, LanguageResponseDTO>
) : ISimpleMapper<CountryEntity, CountryResponseDTO> {

    /**
     * Maps a single `CountryEntity` instance to a `CountryResponseDTO`.
     *
     * @param input The `CountryEntity` to be mapped.
     * @return A mapped `CountryResponseDTO`.
     */
    override fun map(input: CountryEntity): CountryResponseDTO = CountryResponseDTO(
        code = input.code,
        name = input.name,
        flag = input.flag,
        languages = languageMapper.mapList(input.languages).toList()
    )

    /**
     * Maps a list of `CountryEntity` instances to a list of `CountryResponseDTOs`.
     *
     * @param input The `Iterable` collection of `CountryEntity` instances to be mapped.
     * @return A collection of mapped `CountryResponseDTOs`.
     */
    override fun mapList(input: Iterable<CountryEntity>): Iterable<CountryResponseDTO> {
        return input.map(::map)
    }
}