package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.BlockedChannelEntityDAO
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * Mapper implementation for mapping [BlockedChannelEntityDAO] to [SimpleChannelEntity].
 *
 * @property channelMapper The mapper used to map [ChannelEntityDAO] to [SimpleChannelEntity].
 */
class BlockedChannelEntityDaoMapper(
    private val channelMapper: ISimpleMapper<ChannelEntityDAO, SimpleChannelEntity>
) : ISimpleMapper<BlockedChannelEntityDAO, SimpleChannelEntity> {

    /**
     * Maps a [BlockedChannelEntityDAO] object to a [SimpleChannelEntity] object.
     *
     * @param input The input [BlockedChannelEntityDAO] to be mapped.
     * @return A [SimpleChannelEntity] object representing the mapped entity.
     */
    override fun map(input: BlockedChannelEntityDAO): SimpleChannelEntity = with(input) {
        channelMapper.map(channel)
    }

    /**
     * Maps a collection of [BlockedChannelEntityDAO] objects to a collection of [SimpleChannelEntity] objects.
     *
     * @param input The input collection of [BlockedChannelEntityDAO] to be mapped.
     * @return An iterable collection of [SimpleChannelEntity] objects representing the mapped entities.
     */
    override fun mapList(input: Iterable<BlockedChannelEntityDAO>): Iterable<SimpleChannelEntity> =
        input.map(::map)
}
