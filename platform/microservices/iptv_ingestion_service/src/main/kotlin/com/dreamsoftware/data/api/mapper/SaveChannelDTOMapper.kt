package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.iptvorg.model.ChannelDTO

/**
 * Mapper class for converting [ChannelDTO] objects to [SaveChannelEntity] objects.
 * This mapper is responsible for transforming data transfer objects (DTOs) received from
 * a network source into entities that can be stored in a database.
 */
class SaveChannelDTOMapper : ISimpleMapper<ChannelDTO, SaveChannelEntity> {

    /**
     * Maps a single [ChannelDTO] object to a [SaveChannelEntity] object.
     *
     * @param input The input [ChannelDTO] object to be mapped.
     * @return A [SaveChannelEntity] object containing the mapped data.
     */
    override fun map(input: ChannelDTO): SaveChannelEntity = with(input) {
        // Create a new SaveChannelEntity object using properties from ChannelDTO.
        SaveChannelEntity(
            channelId = id,
            name = name,
            network = network,
            country = country,
            subdivision = subdivision,
            city = city,
            isNsfw = isNsfw,
            website = website,
            logo = logo,
            launched = launched,
            closed = closed,
            replacedBy = replacedBy,
            languages = languages,
            categories = categories,
            owners = owners,
            altNames = altNames,
            broadcastArea = broadcastArea
        )
    }

    /**
     * Maps a collection of [ChannelDTO] objects to a collection of [SaveChannelEntity] objects.
     *
     * @param input The iterable collection of [ChannelDTO] objects to be mapped.
     * @return An iterable collection of [SaveChannelEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelDTO>): Iterable<SaveChannelEntity> =
        input.map(::map)
}