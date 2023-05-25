package com.dreamsoftware.data.database.datasource.stream.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ChannelStreamEntityDAO
import com.dreamsoftware.data.database.dao.ChannelStreamTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.stream.IStreamDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.database.entity.ChannelStreamEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class StreamDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<ChannelStreamEntityDAO, ChannelStreamEntity>
): SupportDatabaseDataSource<Long, ChannelStreamEntityDAO, SaveChannelStreamEntity, ChannelStreamEntity>(database, mapper, ChannelStreamEntityDAO), IStreamDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveChannelStreamEntity) = with(entityToSave) {
        this@onMapEntityToSave[ChannelStreamTable.url] = url
        this@onMapEntityToSave[ChannelStreamTable.userAgent] = userAgent
        this@onMapEntityToSave[ChannelStreamTable.httpReferrer] = httpReferrer
        this@onMapEntityToSave[ChannelStreamTable.channelId] = channelId
    }
}