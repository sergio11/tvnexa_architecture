package com.dreamsoftware.data.database.datasource.channel.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class ChannelDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<ChannelEntityDAO, ChannelEntity>
): SupportDatabaseDataSource<String, ChannelEntityDAO, SaveChannelEntity, ChannelEntity>(database, mapper, ChannelEntityDAO), IChannelDatabaseDataSource {

    override suspend fun save(data: SaveChannelEntity) {
        super.save(data)
        with(data) {
            saveCategories(toCategoriesByChannel())
            saveLanguages(toLanguagesByChannel())
        }
    }

    override suspend fun save(data: Iterable<SaveChannelEntity>) {
        super.save(data)
        with(data) {
            saveCategories(fold(listOf()) { items, channel ->
                items + channel.toCategoriesByChannel()
            })
            saveLanguages(fold(listOf()) { items, channel ->
                items + channel.toLanguagesByChannel()
            })
        }
    }

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveChannelEntity) = with(entityToSave) {
        this@onMapEntityToSave[ChannelTable.channelId] = channelId
        this@onMapEntityToSave[ChannelTable.name] = name
        this@onMapEntityToSave[ChannelTable.network] = network
        this@onMapEntityToSave[ChannelTable.country] = country
        this@onMapEntityToSave[ChannelTable.subdivision] = subdivision
        this@onMapEntityToSave[ChannelTable.city] = city
        this@onMapEntityToSave[ChannelTable.isNsfw] = isNsfw
        this@onMapEntityToSave[ChannelTable.launched] = launched
        this@onMapEntityToSave[ChannelTable.closed] = closed
        this@onMapEntityToSave[ChannelTable.replacedBy] = replacedBy
        this@onMapEntityToSave[ChannelTable.website] = website
        this@onMapEntityToSave[ChannelTable.logo] = logo
    }

    private suspend fun saveCategories(data: Iterable<Pair<String, String>>) {
        batchInsertOnDuplicateKeyUpdate(data) {
            this[ChannelCategoryTable.channel] = it.first
            this[ChannelCategoryTable.category] = it.second
        }
    }

    private suspend fun saveLanguages(data: Iterable<Pair<String, String>>) {
        batchInsertOnDuplicateKeyUpdate(data) {
            this[ChannelLanguageTable.channel] = it.first
            this[ChannelLanguageTable.language] = it.second
        }
    }
}