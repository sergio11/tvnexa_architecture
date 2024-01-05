package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.domain.repository.ICountryRepository
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of the [ICountryRepository] interface that provides access to country data
 * from a database data source.
 *
 * @property countryDatabaseDataSource The data source responsible for retrieving country data.
 */
internal class CountryRepositoryImpl(
    private val countryDatabaseDataSource: ICountryDatabaseDataSource
) : ICountryRepository {

    /**
     * Retrieve all countries available in the repository.
     *
     * @return An iterable collection of all country entities.
     */
    override suspend fun findAll(): List<CountryEntity> = withContext(Dispatchers.IO) {
        countryDatabaseDataSource.findAll().toList()
    }

    /**
     * Retrieve a country by its unique code.
     *
     * @param code The unique code of the country to retrieve.
     * @return The country entity matching the specified code.
     */
    /**
     * Retrieve a country by its unique code.
     *
     * @param code The unique code of the country to retrieve.
     * @return The country entity matching the specified code.
     */
    override suspend fun findByCode(code: String): CountryEntity? = withContext(Dispatchers.IO) {
        countryDatabaseDataSource.findByKey(code)
    }
}