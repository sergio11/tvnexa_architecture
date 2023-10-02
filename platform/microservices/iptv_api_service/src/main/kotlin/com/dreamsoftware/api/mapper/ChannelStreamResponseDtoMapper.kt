package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.ChannelStreamResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.ChannelStreamEntity

/**
 * ChannelStreamResponseDtoMapper is a class responsible for mapping instances of ChannelStreamEntity
 * to ChannelStreamResponseDTO objects.
 *
 * This mapper implements the ISimpleMapper interface and provides methods for mapping single entities
 * and lists of entities from the data layer to DTOs in the API layer.
 *
 * @author Sergio Sánchez Sánchez
 * @version 1.0
 */
class ChannelStreamResponseDtoMapper : ISimpleMapper<ChannelStreamEntity, ChannelStreamResponseDTO> {

    /**
     * Maps a single ChannelStreamEntity instance to a ChannelStreamResponseDTO.
     *
     * @param input The ChannelStreamEntity to be mapped.
     * @return A mapped ChannelStreamResponseDTO.
     */
    override fun map(input: ChannelStreamEntity): ChannelStreamResponseDTO = ChannelStreamResponseDTO(
        url = input.url,
        httpReferrer = input.httpReferrer,
        userAgent = input.userAgent
    )

    /**
     * Maps a list of ChannelStreamEntity instances to a list of ChannelStreamResponseDTOs.
     *
     * @param input The Iterable collection of ChannelStreamEntity instances to be mapped.
     * @return A collection of mapped ChannelStreamResponseDTOs.
     */
    override fun mapList(input: Iterable<ChannelStreamEntity>): Iterable<ChannelStreamResponseDTO> =
        input.map(::map)
}
