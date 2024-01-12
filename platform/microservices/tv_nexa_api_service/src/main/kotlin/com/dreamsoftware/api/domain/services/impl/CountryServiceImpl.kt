package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.CountryResponseDTO
import com.dreamsoftware.api.domain.repository.ICountryRepository
import com.dreamsoftware.api.domain.services.ICountryService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import kotlin.jvm.Throws

/**
 * Implementation of the ICountryService interface responsible for managing country-related operations.
 *
 * @property countryRepository The repository responsible for country-related data operations.
 * @property mapper The mapper used to map CountryEntity objects to CountryResponseDTO objects.
 */
internal class CountryServiceImpl(
    private val countryRepository: ICountryRepository,
    private val mapper: ISimpleMapper<CountryEntity, CountryResponseDTO>
): ICountryService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Retrieves a list of all countries.
     *
     * @return A list of CountryResponseDTO objects representing all countries.
     * @throws AppException.InternalServerError if an internal server error occurs while fetching all countries.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<CountryResponseDTO> = withContext(Dispatchers.IO) {
        try {
            countryRepository
                .findAll()
                .map(mapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("COS (findAll) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while fetching all countries.")
        }
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
    override suspend fun findByCode(code: String): CountryResponseDTO = withContext(Dispatchers.IO) {
        try {
            countryRepository.findByCode(code)?.let(mapper::map) ?: run {
                throw AppException.NotFoundException.CountryNotFoundException("Country with code '$code' not found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw if(e !is AppException) {
                log.debug("COS (findByCode) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding country by code.")
            } else {
                e
            }
        }
    }
}