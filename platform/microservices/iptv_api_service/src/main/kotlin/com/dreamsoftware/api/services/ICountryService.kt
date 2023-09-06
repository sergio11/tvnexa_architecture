package com.dreamsoftware.api.services

import com.dreamsoftware.api.dto.CountryResponseDTO
import kotlin.jvm.Throws

interface ICountryService {
    @Throws(CountryServiceException.InternalServerError::class)
    suspend fun findAll(): Iterable<CountryResponseDTO>
    @Throws(CountryServiceException.InternalServerError::class, CountryServiceException.CountryNotFoundException::class)
    suspend fun findByCode(code: String): CountryResponseDTO
}

sealed class CountryServiceException(message: String) : Exception(message) {
    class CountryNotFoundException(code: String) : CountryServiceException("Country with code '$code' not found.")
    class InternalServerError(message: String) : CountryServiceException("Internal server error: $message")
}