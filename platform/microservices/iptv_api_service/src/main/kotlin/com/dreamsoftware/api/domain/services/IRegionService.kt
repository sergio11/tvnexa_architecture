package com.dreamsoftware.api.domain.services

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.RegionResponseDTO

interface IRegionService {

    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): List<RegionResponseDTO>

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.RegionNotFoundException::class
    )
    suspend fun findByCode(code: String): RegionResponseDTO
}

