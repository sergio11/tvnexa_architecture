package com.dreamsoftware.api.services

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.RegionResponseDTO

interface IRegionService {

    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): List<RegionResponseDTO>

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.RegionNotFoundException::class
    )
    suspend fun findByCode(code: String): RegionResponseDTO
}

