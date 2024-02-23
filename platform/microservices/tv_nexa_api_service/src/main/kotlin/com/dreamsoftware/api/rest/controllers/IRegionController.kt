package com.dreamsoftware.api.rest.controllers

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.RegionResponseDTO

/**
 * Interface representing a controller for regions.
 */
interface IRegionController {

    /**
     * Retrieves all regions.
     * @return A list of RegionResponseDTO objects representing all regions.
     * @throws AppException.InternalServerError If an internal server error occurs.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): List<RegionResponseDTO>

    /**
     * Finds a region by its code.
     * @param code The code of the region to find.
     * @return The RegionResponseDTO object representing the found region.
     * @throws AppException.InternalServerError If an internal server error occurs.
     * @throws AppException.NotFoundException.RegionNotFoundException If the specified region is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.RegionNotFoundException::class
    )
    suspend fun findByCode(code: String): RegionResponseDTO
}
