package com.dreamsoftware.data.database.datasource.country.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.ChannelTable
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.CountryLanguageTable
import com.dreamsoftware.data.database.dao.CountryTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import kotlin.reflect.KProperty1

/**
 * Implementation of the [ICountryDatabaseDataSource] interface responsible for handling
 * data operations related to countries in the database.
 *
 * @property database The database factory used to interact with the underlying database.
 * @property mapper The mapper used for mapping between database entities and domain entities.
 */
internal class CountryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<CountryEntityDAO, CountryEntity>
) : SupportDatabaseDataSource<String, CountryEntityDAO, SaveCountryEntity, CountryEntity>(
    database,
    mapper,
    CountryEntityDAO
), ICountryDatabaseDataSource {

    override val findAllEagerRelationships: List<KProperty1<CountryEntityDAO, Any?>>
        get() = listOf(CountryEntityDAO::languages)

    /**
     * Maps the [SaveCountryEntity] to the corresponding [CountryTable] fields
     * for updating the database.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveCountryEntity) = with(entityToSave) {
        this@onMapEntityToSave[CountryTable.code] = code
        this@onMapEntityToSave[CountryTable.name] = name
        this@onMapEntityToSave[CountryTable.flag] = flag
    }

    /**
     * Executed after a transaction finishes saving data to the database.
     * It saves countries' languages relationships to the database.
     *
     * @param data Iterable of [SaveCountryEntity] objects.
     */
    override fun Transaction.onSaveTransactionFinished(data: Iterable<SaveCountryEntity>) {
        log.debug("CountryDatabaseDataSourceImpl onSaveTransactionFinished countries -> ${data.count()}")
        saveCountriesLanguages(data.fold(listOf()) { items, country ->
            items + country.toLanguagesByCountry()
        })
    }

    /**
     * Executed after a transaction finishes saving data for a single entity to the database.
     * It saves a country's languages relationship to the database.
     *
     * @param data [SaveCountryEntity] object.
     */
    override fun Transaction.onSaveTransactionFinished(data: SaveCountryEntity) {
        saveCountriesLanguages(data.toLanguagesByCountry())
    }

    /**
     * Defines the sorting criteria for a [CountryEntityDAO] based on the country name.
     *
     * @param dao The [CountryEntityDAO] instance to determine the sorting criteria for.
     * @return A string representing the country name, which is used as the sorting criterion.
     */
    override fun onSortBy(dao: CountryEntityDAO): String = dao.name

    /**
     * Saves countries' languages relationships to the database.
     *
     * @param data Iterable of pairs representing the relationship between country codes and language codes.
     */
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

    /**
     * Finds countries in the database whose names are similar to the provided search term.
     *
     * @param term The search term used to find countries by name similarity.
     * @return An iterable collection of [CountryEntity] objects containing countries found by name similarity.
     */
    override suspend fun findByNameLike(term: String): Iterable<CountryEntity> = execQuery {
        entityDAO.find {
            if (term.isNotBlank()) {
                CountryTable.name.lowerCase() like "%${term.lowercase()}%"
            } else {
                CountryTable.id.isNotNull()
            }
        }.map(mapper::map)
    }
}