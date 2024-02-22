package com.dreamsoftware.data.database.datasource.country

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SaveCountryEntity

/**
 * Interface for accessing country-related data from a database source.
 * Extends [ISupportDatabaseDataSource] interface.
 */
interface ICountryDatabaseDataSource :
    ISupportDatabaseDataSource<String, SaveCountryEntity, CountryEntity> {

    /**
     * Finds countries whose names are similar to the provided search term.
     *
     * @param term The search term used to find countries by name.
     * @return An iterable collection of [CountryEntity] objects containing countries found by name similarity.
     */
    suspend fun findByNameLike(term: String): Iterable<CountryEntity>
}
