package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.EpgChannelProgrammeEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity

class EpgChannelProgrammeDaoSimpleMapper(
    private val channelMapper: ISimpleMapper<ChannelEntityDAO, ChannelEntity>,
    private val categoryMapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity>
): ISimpleMapper<EpgChannelProgrammeEntityDAO, EpgChannelProgrammeEntity> {

    override fun map(input: EpgChannelProgrammeEntityDAO): EpgChannelProgrammeEntity = with(input) {
        EpgChannelProgrammeEntity(
            id = id.value,
            title  = title,
            channel = channelMapper.map(channel),
            category = category?.let(categoryMapper::map),
            date = date,
            start = start,
            end = end
        )
    }

    override fun mapList(input: Iterable<EpgChannelProgrammeEntityDAO>): Iterable<EpgChannelProgrammeEntity> =
        input.map(::map)
}