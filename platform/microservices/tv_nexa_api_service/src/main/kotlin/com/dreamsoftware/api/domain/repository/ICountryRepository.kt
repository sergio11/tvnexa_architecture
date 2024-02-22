package com.dreamsoftware.api.domain.repository

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
    suspend fun findAll(): List<CountryEntity>

    /**
     * Retrieve a country by its unique code.
     *
     * @param code The unique code of the country to retrieve.
     * @return The country entity matching the specified code.
     */
    suspend fun findByCode(code: String): CountryEntity?

    /**
     * Finds countries by name similarity using the provided search term.
     *
     * @param term The search term used to find countries by name similarity.
     * @return A list of [CountryEntity] objects containing countries found by name similarity.
     */
    suspend fun findByNameLike(term: String): List<CountryEntity>
}