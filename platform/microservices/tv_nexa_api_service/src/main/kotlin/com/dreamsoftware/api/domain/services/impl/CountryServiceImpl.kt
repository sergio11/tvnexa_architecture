package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.ICountryRepository
import com.dreamsoftware.api.domain.services.ICountryService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.response.CountryResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity

/**
 * Implementation of the ICountryService interface responsible for managing country-related operations.
 *
 * @property countryRepository The repository responsible for country-related data operations.
 * @property mapper The mapper used to map CountryEntity objects to CountryResponseDTO objects.
 */
internal class CountryServiceImpl(
    private val countryRepository: ICountryRepository,
    private val mapper: ISimpleMapper<CountryEntity, CountryResponseDTO>
): SupportService(), ICountryService {

    /**
     * Retrieves a list of all countries.
     *
     * @return A list of CountryResponseDTO objects representing all countries.
     * @throws AppException.InternalServerError if an internal server error occurs while fetching all countries.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<CountryResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching all countries.") {
            countryRepository
                .findAll()
                .map(mapper::map)
        }

    /**
     * Retrieves country information by code.
     *
     * @param code The code of the country to retrieve.
     * @return The CountryResponseDTO object representing the country with the specified code.
     * @throws AppException.InternalServerError if an internal server error occurs while finding the country by code.
     * @throws AppException.NotFoundException.CountryNotFoundException if the country with the given code is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.CountryNotFoundException::class
    )
    override suspend fun findByCode(code: String): CountryResponseDTO =
        safeCall(errorMessage = "An error occurred while finding country by code.") {
            countryRepository.findByCode(code)?.let(mapper::map) ?: run {
                throw AppException.NotFoundException.CountryNotFoundException("Country with code '$code' not found.")
            }
        }

    @Throws(AppException.InternalServerError::class)
    override suspend fun findByNameLike(term: String): List<CountryResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching countries.") {
            countryRepository
                .findByNameLike(term)
                .map(mapper::map)
        }
}