package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.CountryEntity

/**
 * Interface for accessing country data from a repository.
 */
interface ICountryRepository {

    /**
     * Retrieve all countries available in the repository.
     *
     * @return An iterable collection of all country entities.
     */
    suspend fun findAll(): Iterable<CountryEntity>

    /**
     * Retrieve a country by its unique code.
     *
     * @param code The unique code of the country to retrieve.
     * @return The country entity matching the specified code.
     */
    suspend fun findByCode(code: String): CountryEntity
}