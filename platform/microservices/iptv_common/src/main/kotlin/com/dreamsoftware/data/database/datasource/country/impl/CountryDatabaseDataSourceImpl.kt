package com.dreamsoftware.data.database.datasource.country.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.CountryTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class CountryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<CountryEntityDAO, CountryEntity>
): SupportDatabaseDataSource<CountryEntityDAO, String, CountryEntity>(database, mapper, CountryEntityDAO), ICountryDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: CountryEntity) = with(entityToSave) {
        this@onMapEntityToSave[CountryTable.code] = code
        this@onMapEntityToSave[CountryTable.name] = name
        this@onMapEntityToSave[CountryTable.flag] = flag
    }
}