package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.dto.CountryResponseDTO
import com.dreamsoftware.api.repository.ICountryRepository
import com.dreamsoftware.api.services.CountryServiceException
import com.dreamsoftware.api.services.ICountryService
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class CountryServiceImpl(
    private val countryRepository: ICountryRepository,
    private val mapper: IMapper<CountryEntity, CountryResponseDTO>
): ICountryService {

    @Throws(CountryServiceException.InternalServerError::class)
    override suspend fun findAll(): Iterable<CountryResponseDTO> = withContext(Dispatchers.IO) {
        try {
            countryRepository.findAll().map(mapper::map)
        } catch (e: Exception) {
            throw CountryServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    @Throws(CountryServiceException.InternalServerError::class, CountryServiceException.CountryNotFoundException::class)
    override suspend fun findByCode(code: String): CountryResponseDTO = withContext(Dispatchers.IO) {
        try {
            val country = countryRepository.findByCode(code)
            if (country != null) {
                mapper.map(country)
            } else {
                throw CountryServiceException.CountryNotFoundException(code)
            }
        } catch (e: Exception) {
            throw CountryServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}