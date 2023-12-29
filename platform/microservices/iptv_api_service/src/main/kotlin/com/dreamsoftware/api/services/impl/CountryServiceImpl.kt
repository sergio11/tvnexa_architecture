package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.CountryResponseDTO
import com.dreamsoftware.api.repository.ICountryRepository
import com.dreamsoftware.api.services.ICountryService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class CountryServiceImpl(
    private val countryRepository: ICountryRepository,
    private val mapper: ISimpleMapper<CountryEntity, CountryResponseDTO>
): ICountryService {

    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): Iterable<CountryResponseDTO> = withContext(Dispatchers.IO) {
        try {
            countryRepository.findAll().map(mapper::map)
        } catch (e: Exception) {
            throw AppException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.CountryNotFoundException::class
    )
    override suspend fun findByCode(code: String): CountryResponseDTO = withContext(Dispatchers.IO) {
        try {
            val country = countryRepository.findByCode(code)
            if (country != null) {
                mapper.map(country)
            } else {
                throw AppException.NotFoundException.CountryNotFoundException(code)
            }
        } catch (e: Exception) {
            throw AppException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}