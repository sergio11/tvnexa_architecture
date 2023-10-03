package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CatchupEntityDAO
import com.dreamsoftware.data.database.entity.CatchupEntity

/**
 * Mapper for converting [CatchupEntityDAO] objects to [CatchupEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([CatchupEntityDAO])
 * to the domain representation ([CatchupEntity]) of a Catchup.
 */
class CatchupEntityDaoSimpleMapper : ISimpleMapper<CatchupEntityDAO, CatchupEntity> {

    /**
     * Maps a single [CatchupEntityDAO] object to a [CatchupEntity] object.
     *
     * @param input The input [CatchupEntityDAO] to map.
     * @return The mapped [CatchupEntity].
     */
    override fun map(input: CatchupEntityDAO): CatchupEntity = with(input) {
        CatchupEntity(
            catchupId = id.value,
            catchupFilePath = storagePath,
            channelId = epgChannelProgramme.channel.channelId,
            programId = epgChannelProgramme.id.value,
            startTime = epgChannelProgramme.start,
            endTime = epgChannelProgramme.end
        )
    }

    /**
     * Maps a list of [CatchupEntityDAO] objects to a list of [CatchupEntity] objects.
     *
     * @param input The input list of [CatchupEntityDAO] objects to map.
     * @return The mapped list of [CatchupEntity] objects.
     */
    override fun mapList(input: Iterable<CatchupEntityDAO>): Iterable<CatchupEntity> =
        input.map(::map)
}