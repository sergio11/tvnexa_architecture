package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.ISubdivisionRepository
import com.dreamsoftware.api.domain.services.ISubdivisionService
import com.dreamsoftware.api.rest.dto.response.SubdivisionResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

/**
 * Implementation of [ISubdivisionService] responsible for managing Subdivisions.
 *
 * @property subdivisionRepository The repository handling Subdivision data.
 * @property subdivisionMapper The mapper converting SubdivisionEntity to SubdivisionResponseDTO.
 */
internal class SubdivisionServiceImpl(
    private val subdivisionRepository: ISubdivisionRepository,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntity, SubdivisionResponseDTO>
) : ISubdivisionService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Retrieves all Subdivision response DTOs.
     *
     * @return A list of SubdivisionResponseDTO objects.
     * @throws AppException.InternalServerError if an internal server error occurs.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<SubdivisionResponseDTO> = withContext(Dispatchers.IO) {
        try {
            subdivisionRepository.findAll()
                .map(subdivisionMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("SUBS (findAll) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while fetching all subdivisions.")
        }
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
    override suspend fun findByCode(code: String): SubdivisionResponseDTO = withContext(Dispatchers.IO) {
        try {
            subdivisionRepository.findByCode(code)?.let(subdivisionMapper::map)
                ?: throw AppException.NotFoundException.SubdivisionNotFoundException(
                    "Subdivision with code '$code' not found."
                )
        } catch (e: Exception) {
            e.printStackTrace()
            throw if(e !is AppException) {
                log.debug("SUBS (findByCode) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding subdivision by code.")
            } else {
                e
            }
        }
    }
}