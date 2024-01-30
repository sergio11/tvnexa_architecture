package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.response.ChannelStreamResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper class that maps [SimpleChannelEntity] objects to [SimpleChannelResponseDTO] objects.
 *
 * @property channelStreamMapper The mapper for mapping [ChannelStreamEntity] objects to [ChannelStreamResponseDTO] objects.
 */
class SimpleChannelResponseDtoMapper(
    private val channelStreamMapper: ISimpleMapper<ChannelStreamEntity, ChannelStreamResponseDTO>,
) : ISimpleMapper<SimpleChannelEntity, SimpleChannelResponseDTO> {

    /**
     * Map a single [SimpleChannelEntity] object to a [SimpleChannelResponseDTO] object.
     *
     * @param input The [SimpleChannelEntity] object to be mapped.
     * @return A [SimpleChannelResponseDTO] object representing the mapped data.
     */
    override fun map(input: SimpleChannelEntity): SimpleChannelResponseDTO = SimpleChannelResponseDTO(
        channelId = input.channelId,
        name = input.name,
        city = input.city,
        isNsfw = input.isNsfw,
        website = input.website,
        logo = input.logo,
        streams = channelStreamMapper.mapList(input.streams).toList()
    )

    /**
     * Map a collection of [SimpleChannelEntity] objects to a collection of [SimpleChannelResponseDTO] objects.
     *
     * @param input The collection of [SimpleChannelEntity] objects to be mapped.
     * @return A collection of [SimpleChannelResponseDTO] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<SimpleChannelEntity>): Iterable<SimpleChannelResponseDTO> =
        input.map(::map)
}