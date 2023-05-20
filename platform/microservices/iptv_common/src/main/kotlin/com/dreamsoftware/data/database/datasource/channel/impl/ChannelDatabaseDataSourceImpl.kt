package com.dreamsoftware.data.database.datasource.channel.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class ChannelDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<ChannelEntityDAO, ChannelEntity>
): SupportDatabaseDataSource<ChannelEntityDAO, String, ChannelEntity>(database, mapper, ChannelEntityDAO), IChannelDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: ChannelEntity) {}
}