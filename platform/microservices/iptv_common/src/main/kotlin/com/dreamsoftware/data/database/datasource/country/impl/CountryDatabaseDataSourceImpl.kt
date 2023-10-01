package com.dreamsoftware.data.database.datasource.country.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.CountryLanguageTable
import com.dreamsoftware.data.database.dao.CountryTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class CountryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<CountryEntityDAO, CountryEntity>
) : SupportDatabaseDataSource<String, CountryEntityDAO, SaveCountryEntity, CountryEntity>(
    database,
    mapper,
    CountryEntityDAO
), ICountryDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveCountryEntity) = with(entityToSave) {
        this@onMapEntityToSave[CountryTable.code] = code
        this@onMapEntityToSave[CountryTable.name] = name
        this@onMapEntityToSave[CountryTable.flag] = flag
    }

    override fun Transaction.onSaveTransactionFinished(data: Iterable<SaveCountryEntity>) {
        saveCountriesLanguages(data.fold(listOf()) { items, country ->
            items + country.toLanguagesByCountry()
        })
    }

    override fun Transaction.onSaveTransactionFinished(data: SaveCountryEntity) {
        saveCountriesLanguages(data.toLanguagesByCountry())
    }

    private fun saveCountriesLanguages(data: Iterable<Pair<String, String>>) {
        CountryLanguageTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(CountryLanguageTable.language, CountryLanguageTable.country),
            data = data,
            onSaveData = {
                this[CountryLanguageTable.country] = it.first
                this[CountryLanguageTable.language] = it.second
            }
        )
    }
}