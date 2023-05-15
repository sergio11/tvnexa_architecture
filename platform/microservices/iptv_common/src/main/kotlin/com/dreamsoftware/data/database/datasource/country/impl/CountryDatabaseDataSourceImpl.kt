package com.dreamsoftware.data.database.datasource.country.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.CountryTable
import org.jetbrains.exposed.sql.ResultRow

internal class CountryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, CountryEntity>
): SupportDatabaseDataSource<CountryEntity, Int>(database, mapper, CountryTable), ICountryDatabaseDataSource