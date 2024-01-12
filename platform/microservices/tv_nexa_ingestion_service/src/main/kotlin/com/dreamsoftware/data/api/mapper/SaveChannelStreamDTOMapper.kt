package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.core.hash256
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.iptvorg.model.ChannelStreamDTO

/**
 * Mapper class for converting [ChannelStreamDTO] objects to [SaveChannelStreamEntity] objects.
 * This mapper is responsible for transforming data transfer objects (DTOs) received from
 * a network source into entities that can be stored in a database.
 */
class SaveChannelStreamDTOMapper : ISimpleMapper<ChannelStreamDTO, SaveChannelStreamEntity> {

    /**
     * Maps a single [ChannelStreamDTO] object to a [SaveChannelStreamEntity] object.
     *
     * @param input The input [ChannelStreamDTO] object to be mapped.
     * @return A [SaveChannelStreamEntity] object containing the mapped data.
     */
    override fun map(input: ChannelStreamDTO): SaveChannelStreamEntity = with(input) {
        // Create a new SaveChannelStreamEntity object using properties from ChannelStreamDTO.
        SaveChannelStreamEntity(
            code = channel.orEmpty().plus(url).hash256(),
            url = url,
            channelId = channel.orEmpty(),
            httpReferrer = httpReferrer,
            userAgent = userAgent
        )
    }

    /**
     * Maps a collection of [ChannelStreamDTO] objects to a collection of [SaveChannelStreamEntity] objects.
     *
     * @param input The iterable collection of [ChannelStreamDTO] objects to be mapped.
     * @return An iterable collection of [SaveChannelStreamEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelStreamDTO>): Iterable<SaveChannelStreamEntity> =
        input.map(::map)
}