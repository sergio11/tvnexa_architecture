package com.dreamsoftware.data.database.datasource.channel.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class ChannelDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<ChannelEntityDAO, ChannelEntity>
) : SupportDatabaseDataSource<String, ChannelEntityDAO, SaveChannelEntity, ChannelEntity>(
    database,
    mapper,
    ChannelEntityDAO
), IChannelDatabaseDataSource {

    override suspend fun save(data: Iterable<SaveChannelEntity>) {
        super.save(data.filter { it.replacedBy == null })
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

    override fun Transaction.onSaveTransactionFinished(data: Iterable<SaveChannelEntity>) {
        with(data) {
            ChannelTable.batchInsertOnDuplicateKeyUpdate(onDupUpdateColumns = listOf(ChannelTable.id), data = data.filter { it.replacedBy != null }) {
                onMapEntityToSave(it)
            }
            /*saveCategories(fold(listOf()) { items, channel ->
                items + channel.toCategoriesByChannel()
            })*/
            /*saveLanguages(fold(listOf()) { items, channel ->
                items + channel.toLanguagesByChannel()
            })*/
        }
    }

    override fun Transaction.onSaveTransactionFinished(data: SaveChannelEntity) {
        with(data) {
            saveCategories(toCategoriesByChannel())
            saveLanguages(toLanguagesByChannel())
        }
    }

    private fun saveCategories(data: Iterable<Pair<String, String>>) {
        ChannelCategoryTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelCategoryTable.category,
                ChannelCategoryTable.channel
            ), data = data
        ) {
            this[ChannelCategoryTable.channel] = it.first
            this[ChannelCategoryTable.category] = it.second
        }
    }

    private fun saveLanguages(data: Iterable<Pair<String, String>>) {
        ChannelLanguageTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelLanguageTable.channel,
                ChannelLanguageTable.language
            ), data = data
        ) {
            this[ChannelLanguageTable.channel] = it.first
            this[ChannelLanguageTable.language] = it.second
        }
    }
}