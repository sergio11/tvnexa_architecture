package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.response.LanguageResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.LanguageEntity

/**
 * `LanguageResponseDtoMapper` is a class responsible for mapping instances of `LanguageEntity`
 * to `LanguageResponseDTO` objects. It encapsulates the logic for transforming language-related data
 * from the data layer to DTOs for use in the API layer.
 *
 * This mapper implements the `ISimpleMapper` interface and provides methods for mapping single entities
 * and lists of entities from the data layer to DTOs in the API layer.
 *
 * @author Sergio Sánchez Sánchez
 * @version 1.0
 */
class LanguageResponseDtoMapper : ISimpleMapper<LanguageEntity, LanguageResponseDTO> {

    /**
     * Maps a single `LanguageEntity` instance to a `LanguageResponseDTO`.
     *
     * @param input The `LanguageEntity` to be mapped.
     * @return A mapped `LanguageResponseDTO`.
     */
    override fun map(input: LanguageEntity): LanguageResponseDTO = LanguageResponseDTO(
        code = input.code,
        name = input.name
    )

    /**
     * Maps a list of `LanguageEntity` instances to a list of `LanguageResponseDTOs`.
     *
     * @param input The `Iterable` collection of `LanguageEntity` instances to be mapped.
     * @return A collection of mapped `LanguageResponseDTOs`.
     */
    override fun mapList(input: Iterable<LanguageEntity>): Iterable<LanguageResponseDTO> =
        input.map(::map)
}