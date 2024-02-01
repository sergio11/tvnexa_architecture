package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.ISubdivisionRepository
import com.dreamsoftware.api.domain.services.ISubdivisionService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.response.SubdivisionResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SubdivisionEntity

/**
 * Implementation of [ISubdivisionService] responsible for managing Subdivisions.
 *
 * @property subdivisionRepository The repository handling Subdivision data.
 * @property subdivisionMapper The mapper converting SubdivisionEntity to SubdivisionResponseDTO.
 */
internal class SubdivisionServiceImpl(
    private val subdivisionRepository: ISubdivisionRepository,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntity, SubdivisionResponseDTO>
) : SupportService(), ISubdivisionService {

    /**
     * Retrieves all Subdivision response DTOs.
     *
     * @return A list of SubdivisionResponseDTO objects.
     * @throws AppException.InternalServerError if an internal server error occurs.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<SubdivisionResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching all subdivisions.") {
            subdivisionRepository.findAll()
                .map(subdivisionMapper::map)
        }

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
    override suspend fun findByCode(code: String): SubdivisionResponseDTO =
        safeCall(errorMessage = "An error occurred while finding subdivision by code.") {
            subdivisionRepository.findByCode(code)?.let(subdivisionMapper::map)
                ?: throw AppException.NotFoundException.SubdivisionNotFoundException(
                    "Subdivision with code '$code' not found."
                )
        }
}