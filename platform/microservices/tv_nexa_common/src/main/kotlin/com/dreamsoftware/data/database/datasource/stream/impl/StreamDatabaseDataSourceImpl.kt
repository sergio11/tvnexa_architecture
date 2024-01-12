package com.dreamsoftware.data.database.datasource.stream.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ChannelStreamEntityDAO
import com.dreamsoftware.data.database.dao.ChannelStreamTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.stream.IStreamDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.database.entity.ChannelStreamEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Implementation of the [IStreamDatabaseDataSource] interface for managing channel stream data in a database.
 *
 * @param database The [IDatabaseFactory] instance for accessing the database.
 * @param mapper The mapper to convert between the database entity ([ChannelStreamEntityDAO]) and domain entity ([ChannelStreamEntity]).
 */
internal class StreamDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<ChannelStreamEntityDAO, ChannelStreamEntity>
) : SupportDatabaseDataSource<String, ChannelStreamEntityDAO, SaveChannelStreamEntity, ChannelStreamEntity>(database, mapper, ChannelStreamEntityDAO),
    IStreamDatabaseDataSource {

    override val disableFkValidationsOnBatchOperation: Boolean
        get() = true

    /**
     * Saves a single channel stream entity to the database.
     *
     * @param data The [SaveChannelStreamEntity] to be saved.
     */
    override suspend fun save(data: SaveChannelStreamEntity) {
        if (data.channelId.isNotBlank()) {
            super.save(data)
        }
    }

    /**
     * Saves multiple channel stream entities to the database.
     *
     * @param data The collection of [SaveChannelStreamEntity] instances to be saved.
     */
    override suspend fun save(data: Iterable<SaveChannelStreamEntity>) {
        super.save(data.filter { it.channelId.isNotBlank() })
    }

    /**
     * Maps a [SaveChannelStreamEntity] to a database update builder for saving to the database.
     *
     * @param entityToSave The [SaveChannelStreamEntity] to be mapped.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveChannelStreamEntity) = with(entityToSave) {
        this@onMapEntityToSave[ChannelStreamTable.code] = code
        this@onMapEntityToSave[ChannelStreamTable.url] = url
        this@onMapEntityToSave[ChannelStreamTable.userAgent] = userAgent
        this@onMapEntityToSave[ChannelStreamTable.httpReferrer] = httpReferrer
        this@onMapEntityToSave[ChannelStreamTable.channel] = channelId
    }
}