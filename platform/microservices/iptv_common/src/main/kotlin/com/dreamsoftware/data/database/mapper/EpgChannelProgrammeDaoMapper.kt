package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.dao.EpgChannelProgrammeEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity

class EpgChannelProgrammeDaoMapper(
    private val channelMapper: IMapper<ChannelEntityDAO, ChannelEntity>,
    private val categoryMapper: IMapper<CategoryEntityDAO, CategoryEntity>
): IMapper<EpgChannelProgrammeEntityDAO, EpgChannelProgrammeEntity> {

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