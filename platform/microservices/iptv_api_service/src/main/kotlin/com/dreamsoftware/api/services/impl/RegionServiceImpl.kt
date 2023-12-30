package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.RegionResponseDTO
import com.dreamsoftware.api.repository.IRegionRepository
import com.dreamsoftware.api.services.IRegionService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.RegionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

class RegionServiceImpl(
    private val regionRepository: IRegionRepository,
    private val regionMapper: ISimpleMapper<RegionEntity, RegionResponseDTO>
) : IRegionService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): Iterable<RegionResponseDTO> = withContext(Dispatchers.IO) {
        try {
            regionRepository
                .findAll()
                .map(regionMapper::map)
        } catch (e: Exception) {
            log.debug("RES (findAll) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while fetching all regions.")
        }
    }

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.RegionNotFoundException::class
    )
    override suspend fun findByCode(code: String): RegionResponseDTO = withContext(Dispatchers.IO) {
        try {
            regionRepository.findByCode(code)?.let(regionMapper::map) ?: run {
                throw AppException.NotFoundException.RegionNotFoundException("Region with code '$code' not found.")
            }
        } catch (e: Exception) {
            log.debug("RES (findByCode) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding region by code.")
        }
    }
}