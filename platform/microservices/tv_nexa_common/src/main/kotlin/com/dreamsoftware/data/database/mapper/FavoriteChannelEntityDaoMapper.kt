package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.FavoriteChannelEntityDAO
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * Mapper implementation for mapping [FavoriteChannelEntityDAO] to [SimpleChannelEntity].
 *
 * @property channelMapper The mapper used to map [ChannelEntityDAO] to [SimpleChannelEntity].
 */
class FavoriteChannelEntityDaoMapper(
    private val channelMapper: ISimpleMapper<ChannelEntityDAO, SimpleChannelEntity>
) : ISimpleMapper<FavoriteChannelEntityDAO, SimpleChannelEntity> {

    /**
     * Maps a [FavoriteChannelEntityDAO] object to a [SimpleChannelEntity] object.
     *
     * @param input The input [FavoriteChannelEntityDAO] to be mapped.
     * @return A [SimpleChannelEntity] object representing the mapped entity.
     */
    override fun map(input: FavoriteChannelEntityDAO): SimpleChannelEntity = with(input) {
        channelMapper.map(channel)
    }

    /**
     * Maps a collection of [FavoriteChannelEntityDAO] objects to a collection of [SimpleChannelEntity] objects.
     *
     * @param input The input collection of [FavoriteChannelEntityDAO] to be mapped.
     * @return An iterable collection of [SimpleChannelEntity] objects representing the mapped entities.
     */
    override fun mapList(input: Iterable<FavoriteChannelEntityDAO>): Iterable<SimpleChannelEntity> =
        input.map(::map)
}
