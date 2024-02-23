package com.dreamsoftware.api.rest.controllers

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.SubdivisionResponseDTO

/**
 * Service interface defining operations related to Subdivisions.
 */
interface ISubdivisionController {

    /**
     * Retrieves all Subdivision response DTOs.
     *
     * @return A list of SubdivisionResponseDTO objects.
     * @throws AppException.InternalServerError if an internal server error occurs.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): List<SubdivisionResponseDTO>

    /**
     * Retrieves a Subdivision response DTO by its code.
     *
     * @param code The unique code identifying the Subdivision.
     * @return The SubdivisionResponseDTO object if found.
     * @throws AppException.InternalServerError if an internal server error occurs.
     * @throws AppException.NotFoundException.SubdivisionNotFoundException if the Subdivision is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.SubdivisionNotFoundException::class
    )
    suspend fun findByCode(code: String): SubdivisionResponseDTO
}