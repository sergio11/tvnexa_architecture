package com.dreamsoftware.data.database.datasource.guide.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ChannelGuideEntityDAO
import com.dreamsoftware.data.database.dao.ChannelGuideTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.guide.IChannelGuideDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelGuideEntity
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class ChannelGuideDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<ChannelGuideEntityDAO, ChannelGuideEntity>
): SupportDatabaseDataSource<Long, ChannelGuideEntityDAO, SaveChannelGuideEntity, ChannelGuideEntity>(database, mapper, ChannelGuideEntityDAO), IChannelGuideDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveChannelGuideEntity) = with(entityToSave) {
        this@onMapEntityToSave[ChannelGuideTable.channel] = channel
        this@onMapEntityToSave[ChannelGuideTable.site] = site
        this@onMapEntityToSave[ChannelGuideTable.siteId] = siteId
        this@onMapEntityToSave[ChannelGuideTable.siteName] = siteName
        this@onMapEntityToSave[ChannelGuideTable.lang] = lang
    }

    override suspend fun findByChannelId(channelId: String): Iterable<ChannelGuideEntity> = execQuery {
        entityDAO.find { ChannelGuideTable.channel eq channelId }.map(mapper::map)
    }

    override suspend fun findByLanguageId(languageId: String): Iterable<ChannelGuideEntity> = execQuery {
        entityDAO.find { ChannelGuideTable.lang eq languageId }.distinctBy { ChannelGuideTable.site }.map(mapper::map)
    }
}