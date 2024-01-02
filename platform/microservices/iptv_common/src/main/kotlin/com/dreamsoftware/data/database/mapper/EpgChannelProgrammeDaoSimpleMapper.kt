package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.EpgChannelProgrammeEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity

/**
 * Mapper for converting [EpgChannelProgrammeEntityDAO] objects to [EpgChannelProgrammeEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([EpgChannelProgrammeEntityDAO])
 * to the domain representation ([EpgChannelProgrammeEntity]) of an EPG channel programme.
 *
 * @param channelMapper A mapper for converting [ChannelEntityDAO] to [ChannelEntity].
 * @param categoryMapper A mapper for converting [CategoryEntityDAO] to [CategoryEntity].
 */
class EpgChannelProgrammeDaoSimpleMapper(
    private val channelMapper: ISimpleMapper<ChannelEntityDAO, ChannelEntity>,
    private val categoryMapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity>
) : ISimpleMapper<EpgChannelProgrammeEntityDAO, EpgChannelProgrammeEntity> {

    /**
     * Maps a single [EpgChannelProgrammeEntityDAO] object to an [EpgChannelProgrammeEntity] object.
     *
     * @param input The input [EpgChannelProgrammeEntityDAO] to map.
     * @return The mapped [EpgChannelProgrammeEntity].
     */
    override fun map(input: EpgChannelProgrammeEntityDAO): EpgChannelProgrammeEntity = with(input) {
        EpgChannelProgrammeEntity(
            id = id.value,
            title = title,
            channel = channel?.let(channelMapper::map),
            category = category?.let(categoryMapper::map),
            date = date,
            start = start,
            end = end
        )
    }

    /**
     * Maps a list of [EpgChannelProgrammeEntityDAO] objects to a list of [EpgChannelProgrammeEntity] objects.
     *
     * @param input The input list of [EpgChannelProgrammeEntityDAO] objects to map.
     * @return The mapped list of [EpgChannelProgrammeEntity] objects.
     */
    override fun mapList(input: Iterable<EpgChannelProgrammeEntityDAO>): Iterable<EpgChannelProgrammeEntity> =
        input.map(::map)
}