package com.dreamsoftware.api.domain.services

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.CountryResponseDTO

/**
 * Interface for managing countries.
 */
interface ICountryService {

    /**
     * Retrieves all countries.
     *
     * @return Iterable of CountryResponseDTO containing all countries.
     * @throws AppException.InternalServerError if there's an internal server error.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): List<CountryResponseDTO>

    /**
     * Retrieves a country by its code.
     *
     * @param code The code of the country to retrieve.
     * @return CountryResponseDTO containing the country details.
     * @throws AppException.InternalServerError if there's an internal server error.
     * @throws AppException.NotFoundException.CountryNotFoundException if the country is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.CountryNotFoundException::class
    )
    suspend fun findByCode(code: String): CountryResponseDTO

    /**
     * Finds countries whose names are similar to the provided search term.
     *
     * @param term The search term used to find countries by name.
     * @return A list of [CountryResponseDTO] objects containing countries found by name similarity.
     * @throws AppException.InternalServerError If an internal server error occurs during the search process.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findByNameLike(term: String): List<CountryResponseDTO>
}