package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.ICountryRepository
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity

/**
 * Implementation of the [ICountryRepository] interface that provides access to country data
 * from a database data source.
 *
 * @property countryDatabaseDataSource The data source responsible for retrieving country data.
 * @property cacheDatasource The cache data source used for caching channel data.
 *
 */
internal class CountryRepositoryImpl(
    private val countryDatabaseDataSource: ICountryDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), ICountryRepository {

    private companion object {
        const val ALL_COUNTRIES_CACHE_KEY = "countries:all"
        const val COUNTRY_CACHE_KEY_PREFIX = "countries:"
        const val COUNTRY_SEARCH_CACHE_KEY_PREFIX = "countries:search:"
    }

    /**
     * Retrieve all countries available in the repository.
     *
     * @return An iterable collection of all country entities.
     */
    override suspend fun findAll(): List<CountryEntity> =
        retrieveFromCacheOrElse(cacheKey = ALL_COUNTRIES_CACHE_KEY) {
            countryDatabaseDataSource.findAll().toList()
        }

    /**
     * Retrieve a country by its unique code.
     *
     * @param code The unique code of the country to retrieve.
     * @return The country entity matching the specified code.
     */
    override suspend fun findByCode(code: String): CountryEntity? =
        retrieveFromCacheOrElse(cacheKey = COUNTRY_CACHE_KEY_PREFIX) {
            countryDatabaseDataSource.findByKey(code)
        }

    /**
     * Finds countries by name similarity using the provided search term.
     * Retrieves data from cache if available; otherwise, retrieves data from the database and caches it.
     *
     * @param term The search term used to find countries by name similarity.
     * @return A list of [CountryEntity] objects containing countries found by name similarity.
     */
    override suspend fun findByNameLike(term: String): List<CountryEntity> =
        retrieveFromCacheOrElse(cacheKey = COUNTRY_SEARCH_CACHE_KEY_PREFIX + term) {
            countryDatabaseDataSource.findByNameLike(term).toList()
        }
}