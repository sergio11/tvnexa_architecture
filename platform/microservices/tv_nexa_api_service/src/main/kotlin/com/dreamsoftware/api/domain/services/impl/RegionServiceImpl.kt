package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IRegionRepository
import com.dreamsoftware.api.domain.services.IRegionService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.response.RegionResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.RegionEntity

/**
 * Implementation of the IRegionService interface responsible for managing region-related operations.
 *
 * @property regionRepository The repository responsible for region-related data operations.
 * @property regionMapper The mapper used to map RegionEntity objects to RegionResponseDTO objects.
 */
internal class RegionServiceImpl(
    private val regionRepository: IRegionRepository,
    private val regionMapper: ISimpleMapper<RegionEntity, RegionResponseDTO>
) : SupportService(), IRegionService {

    /**
     * Retrieves a list of all regions.
     *
     * @return A list of RegionResponseDTO objects representing all regions.
     * @throws AppException.InternalServerError if an internal server error occurs while fetching all regions.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<RegionResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching all regions.") {
            regionRepository
                .findAll()
                .map(regionMapper::map)
        }

    /**
     * Retrieves region information by code.
     *
     * @param code The code of the region to retrieve.
     * @return The RegionResponseDTO object representing the region with the specified code.
     * @throws AppException.InternalServerError if an internal server error occurs while finding the region by code.
     * @throws AppException.NotFoundException.RegionNotFoundException if the region with the given code is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.RegionNotFoundException::class
    )
    override suspend fun findByCode(code: String): RegionResponseDTO =
        safeCall(errorMessage = "An error occurred while finding region by code.") {
            regionRepository.findByCode(code)?.let(regionMapper::map) ?: run {
                throw AppException.NotFoundException.RegionNotFoundException("Region with code '$code' not found.")
            }
        }
}