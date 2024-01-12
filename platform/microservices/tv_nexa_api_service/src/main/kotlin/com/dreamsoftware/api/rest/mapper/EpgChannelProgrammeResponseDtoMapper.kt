package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.response.CategoryResponseDTO
import com.dreamsoftware.api.rest.dto.response.EpgChannelProgrammeResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * `EpgChannelProgrammeResponseDtoMapper` is a class responsible for mapping instances of `EpgChannelProgrammeEntity`
 * to `EpgChannelProgrammeResponseDTO` objects. It encapsulates the logic for transforming electronic program guide (EPG)
 * channel program data from the data layer to DTOs for use in the API layer.
 *
 * This mapper implements the `ISimpleMapper` interface and provides methods for mapping single entities
 * and lists of entities from the data layer to DTOs in the API layer.
 *
 * @param channelMapper A mapper for converting `ChannelEntity` instances to `ChannelResponseDTO`.
 * @param categoryMapper A mapper for converting `CategoryEntity` instances to `CategoryResponseDTO`.
 *
 * @author Sergio Sánchez Sánchez
 * @version 1.0
 */
class EpgChannelProgrammeResponseDtoMapper(
    private val channelMapper: ISimpleMapper<SimpleChannelEntity, SimpleChannelResponseDTO>,
    private val categoryMapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>
) : ISimpleMapper<EpgChannelProgrammeEntity, EpgChannelProgrammeResponseDTO> {

    /**
     * Maps a single `EpgChannelProgrammeEntity` instance to an `EpgChannelProgrammeResponseDTO`.
     *
     * @param input The `EpgChannelProgrammeEntity` to be mapped.
     * @return A mapped `EpgChannelProgrammeResponseDTO`.
     */
    override fun map(input: EpgChannelProgrammeEntity): EpgChannelProgrammeResponseDTO = EpgChannelProgrammeResponseDTO(
        id = input.id,
        title = input.title,
        channel = input.channel?.let(channelMapper::map),
        category = input.category?.let(categoryMapper::map),
        start = input.start.toString(),
        end = input.end.toString(),
        date = input.date.toString()
    )

    /**
     * Maps a list of `EpgChannelProgrammeEntity` instances to a list of `EpgChannelProgrammeResponseDTOs`.
     *
     * @param input The `Iterable` collection of `EpgChannelProgrammeEntity` instances to be mapped.
     * @return A collection of mapped `EpgChannelProgrammeResponseDTOs`.
     */
    override fun mapList(input: Iterable<EpgChannelProgrammeEntity>): Iterable<EpgChannelProgrammeResponseDTO> =
        input.map(::map)
}