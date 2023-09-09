package com.dreamsoftware.data.database.datasource.channel

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity

interface IChannelDatabaseDataSource: ISupportDatabaseDataSource<String, SaveChannelEntity, ChannelEntity> {

    fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<ChannelEntity>

}