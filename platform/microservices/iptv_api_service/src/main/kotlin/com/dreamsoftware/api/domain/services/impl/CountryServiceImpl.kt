package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.CountryResponseDTO
import com.dreamsoftware.api.domain.repository.ICountryRepository
import com.dreamsoftware.api.domain.services.ICountryService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import kotlin.jvm.Throws

class CountryServiceImpl(
    private val countryRepository: ICountryRepository,
    private val mapper: ISimpleMapper<CountryEntity, CountryResponseDTO>
): ICountryService {

    private val log = LoggerFactory.getLogger(this::class.java)

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
            log.debug("COS (findByCode) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding country by code.")
        }
    }
}