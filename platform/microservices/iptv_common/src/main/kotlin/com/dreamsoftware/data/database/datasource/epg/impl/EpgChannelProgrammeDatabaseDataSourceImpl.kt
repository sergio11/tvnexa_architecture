package com.dreamsoftware.data.database.datasource.epg.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.EpgChannelProgrammeEntityDAO
import com.dreamsoftware.data.database.dao.EpgChannelProgrammeTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.LocalDateTime

internal class EpgChannelProgrammeDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<EpgChannelProgrammeEntityDAO, EpgChannelProgrammeEntity>
) : SupportDatabaseDataSource<Long, EpgChannelProgrammeEntityDAO, SaveEpgChannelProgrammeEntity, EpgChannelProgrammeEntity>(
    database,
    mapper,
    EpgChannelProgrammeEntityDAO
), IEpgChannelProgrammeDatabaseDataSource {
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveEpgChannelProgrammeEntity) = with(entityToSave) {
        this@onMapEntityToSave[EpgChannelProgrammeTable.channelId] = channel
        this@onMapEntityToSave[EpgChannelProgrammeTable.category] = category
        this@onMapEntityToSave[EpgChannelProgrammeTable.title] = title
        this@onMapEntityToSave[EpgChannelProgrammeTable.start] = start
        this@onMapEntityToSave[EpgChannelProgrammeTable.end] = end
        this@onMapEntityToSave[EpgChannelProgrammeTable.date] = date
    }

    override suspend fun findByChannelIdAndDateRange(channelId: String, startAt: LocalDateTime, endAt: LocalDateTime): Iterable<EpgChannelProgrammeEntity> = execQuery {
        entityDAO.find {
            (EpgChannelProgrammeTable.channelId eq channelId) and
                    (EpgChannelProgrammeTable.start greaterEq startAt) and
                    (EpgChannelProgrammeTable.end lessEq endAt )
        }.map(mapper::map)
    }
}