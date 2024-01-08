package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.ChannelGuideResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.ChannelGuideEntity

/**
 * Mapper class that maps [ChannelGuideEntity] objects to [ChannelGuideResponseDTO] objects.
 */
class ChannelGuideResponseDtoMapper : ISimpleMapper<ChannelGuideEntity, ChannelGuideResponseDTO> {

    /**
     * Map a single [ChannelGuideEntity] object to a [ChannelGuideResponseDTO] object.
     *
     * @param input The [ChannelGuideEntity] object to be mapped.
     * @return A [ChannelGuideResponseDTO] object representing the mapped data.
     */
    override fun map(input: ChannelGuideEntity): ChannelGuideResponseDTO = ChannelGuideResponseDTO(
        id = input.id,
        site = input.site,
        siteId = input.siteId,
        siteName = input.siteName,
        lang = input.lang
    )

    /**
     * Map a collection of [ChannelGuideEntity] objects to a collection of [ChannelGuideResponseDTO] objects.
     *
     * @param input The collection of [ChannelGuideEntity] objects to be mapped.
     * @return A collection of [ChannelGuideResponseDTO] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelGuideEntity>): Iterable<ChannelGuideResponseDTO> =
        input.map(::map)
}