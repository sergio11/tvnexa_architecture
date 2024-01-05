package com.dreamsoftware.api.domain.services

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.CountryResponseDTO
import kotlin.jvm.Throws

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
}