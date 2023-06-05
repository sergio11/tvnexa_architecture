package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.core.toLocalDateTime
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity
import com.dreamsoftware.data.epg.model.EpgDataDTO

class SaveEpgDataDTOMapper: IMapper<EpgDataDTO, SaveEpgChannelProgrammeEntity> {

    override fun map(input: EpgDataDTO): SaveEpgChannelProgrammeEntity = with(input) {
        SaveEpgChannelProgrammeEntity(
            title = title,
            channel = channelId,
            category = category,
            start = start.toLocalDateTime(),
            end = stop.toLocalDateTime(),
            date = date.toLocalDateTime()
        )
    }

    override fun mapList(input: Iterable<EpgDataDTO>): Iterable<SaveEpgChannelProgrammeEntity> =
        input.map(::map)
}