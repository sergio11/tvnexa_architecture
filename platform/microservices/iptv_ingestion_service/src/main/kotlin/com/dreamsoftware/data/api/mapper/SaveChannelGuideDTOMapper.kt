package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity
import com.dreamsoftware.data.iptvorg.model.ChannelGuideDTO

/**
 * Mapper class for converting [ChannelGuideDTO] objects to [SaveChannelGuideEntity] objects.
 * This mapper is responsible for transforming data transfer objects (DTOs) received from
 * a network source into entities that can be stored in a database.
 */
class SaveChannelGuideDTOMapper : ISimpleMapper<ChannelGuideDTO, SaveChannelGuideEntity> {

    /**
     * Maps a single [ChannelGuideDTO] object to a [SaveChannelGuideEntity] object.
     *
     * @param input The input [ChannelGuideDTO] object to be mapped.
     * @return A [SaveChannelGuideEntity] object containing the mapped data.
     */
    override fun map(input: ChannelGuideDTO): SaveChannelGuideEntity = with(input) {
        // Create a new SaveChannelGuideEntity object using properties from ChannelGuideDTO.
        SaveChannelGuideEntity(
            site = site,
            siteId = siteId,
            siteName = siteName,
            channel = channel,
            lang = lang
        )
    }

    /**
     * Maps a collection of [ChannelGuideDTO] objects to a collection of [SaveChannelGuideEntity] objects.
     *
     * @param input The iterable collection of [ChannelGuideDTO] objects to be mapped.
     * @return An iterable collection of [SaveChannelGuideEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelGuideDTO>): Iterable<SaveChannelGuideEntity> =
        input.map(::map)
}