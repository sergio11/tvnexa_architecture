package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.core.toLocalDateTime
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity
import com.dreamsoftware.data.epg.model.EpgDataDTO

/**
 * Mapper class for converting [EpgDataDTO] objects to [SaveEpgChannelProgrammeEntity] objects.
 * This mapper is responsible for transforming Electronic Program Guide (EPG) data transfer
 * objects (DTOs) into entities that can be stored in a database.
 */
class SaveEpgDataDTOMapper : ISimpleMapper<EpgDataDTO, SaveEpgChannelProgrammeEntity> {

    /**
     * Maps a single [EpgDataDTO] object to a [SaveEpgChannelProgrammeEntity] object.
     *
     * @param input The input [EpgDataDTO] object to be mapped.
     * @return A [SaveEpgChannelProgrammeEntity] object containing the mapped data.
     */
    override fun map(input: EpgDataDTO): SaveEpgChannelProgrammeEntity = with(input) {
        // Create a new SaveEpgChannelProgrammeEntity object using properties from EpgDataDTO.
        SaveEpgChannelProgrammeEntity(
            title = title,
            channel = channelId,
            category = category,
            start = start.toLocalDateTime(), // Convert start time to LocalDateTime
            end = stop.toLocalDateTime(),    // Convert end time to LocalDateTime
            date = date.toLocalDateTime(),   // Convert date to LocalDateTime,
            site = site,
            language = lang
        )
    }

    /**
     * Maps a collection of [EpgDataDTO] objects to a collection of [SaveEpgChannelProgrammeEntity] objects.
     *
     * @param input The iterable collection of [EpgDataDTO] objects to be mapped.
     * @return An iterable collection of [SaveEpgChannelProgrammeEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<EpgDataDTO>): Iterable<SaveEpgChannelProgrammeEntity> =
        input.map(::map)
}