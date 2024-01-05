package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.*
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper class that maps [ChannelEntity] objects to [SimpleChannelResponseDTO] objects.
 *
 */
class SimpleChannelResponseDtoMapper : ISimpleMapper<ChannelEntity, SimpleChannelResponseDTO> {

    /**
     * Map a single [ChannelEntity] object to a [SimpleChannelResponseDTO] object.
     *
     * @param input The [ChannelEntity] object to be mapped.
     * @return A [SimpleChannelResponseDTO] object representing the mapped data.
     */
    override fun map(input: ChannelEntity): SimpleChannelResponseDTO = SimpleChannelResponseDTO(
        channelId = input.channelId,
        name = input.name,
        city = input.city,
        isNsfw = input.isNsfw,
        website = input.website,
        logo = input.logo
    )

    /**
     * Map a collection of [ChannelEntity] objects to a collection of [SimpleChannelResponseDTO] objects.
     *
     * @param input The collection of [ChannelEntity] objects to be mapped.
     * @return A collection of [SimpleChannelResponseDTO] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelEntity>): Iterable<SimpleChannelResponseDTO> =
        input.map(::map)
}