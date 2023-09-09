package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.ChannelEntity

interface IChannelRepository {
    suspend fun findAll(): Iterable<ChannelEntity>
    suspend fun findById(id: String): ChannelEntity
    suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<ChannelEntity>
}