package com.dreamsoftware.data.database.datasource.country.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.CountryTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class CountryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, CountryEntity>
): SupportDatabaseDataSource<CountryEntity, String>(database, mapper, CountryTable), ICountryDatabaseDataSource {
    override fun UpdateBuilder<Int>.onMapEntityToSave(entity: CountryEntity) = with(entity) {
        this@onMapEntityToSave[CountryTable.code] = code
        this@onMapEntityToSave[CountryTable.name] = name
        this@onMapEntityToSave[CountryTable.flag] = flag
        this@onMapEntityToSave[CountryTable.languages] = languages.joinToString(",")
    }
}