package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.api.repository.IChannelRepository
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity

class ChannelRepositoryImpl(
    private val channelDataSource: IChannelDatabaseDataSource
): IChannelRepository {
    override suspend fun findAll(): Iterable<ChannelEntity> =
        channelDataSource.findAll()

    override suspend fun findById(id: String): ChannelEntity =
        channelDataSource.findByKey(id)

    override suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<ChannelEntity> =
        channelDataSource.filterByCategoryAndCountry(categoryId, countryId)
}