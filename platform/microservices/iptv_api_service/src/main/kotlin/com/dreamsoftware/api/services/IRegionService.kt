package com.dreamsoftware.api.services

import com.dreamsoftware.api.dto.RegionResponseDTO

interface IRegionService {
    @Throws(RegionServiceException.InternalServerError::class)
    suspend fun findAll(): Iterable<RegionResponseDTO>
    @Throws(RegionServiceException.InternalServerError::class, RegionServiceException.RegionNotFoundException::class)
    suspend fun findByCode(code: String): RegionResponseDTO
}

sealed class RegionServiceException(message: String) : Exception(message) {
    class RegionNotFoundException(code: String) : RegionServiceException("Region with code '$code' not found.")
    class InternalServerError(message: String) : RegionServiceException("Internal server error: $message")
}