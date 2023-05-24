package com.dreamsoftware.data.database.datasource.stream.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.StreamEntityDAO
import com.dreamsoftware.data.database.dao.StreamTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.stream.IStreamDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveStreamEntity
import com.dreamsoftware.data.database.entity.StreamEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class StreamDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<StreamEntityDAO, StreamEntity>
): SupportDatabaseDataSource<Long, StreamEntityDAO, SaveStreamEntity, StreamEntity>(database, mapper, StreamEntityDAO), IStreamDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveStreamEntity) = with(entityToSave) {
        this@onMapEntityToSave[StreamTable.url] = url
        this@onMapEntityToSave[StreamTable.userAgent] = userAgent
        this@onMapEntityToSave[StreamTable.httpReferrer] = httpReferrer
        this@onMapEntityToSave[StreamTable.channelId] = channelId
    }
}