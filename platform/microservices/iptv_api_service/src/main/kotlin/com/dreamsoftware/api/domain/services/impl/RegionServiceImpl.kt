package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.RegionResponseDTO
import com.dreamsoftware.api.domain.repository.IRegionRepository
import com.dreamsoftware.api.domain.services.IRegionService
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
    override suspend fun findAll(): List<RegionResponseDTO> = withContext(Dispatchers.IO) {
        try {
            regionRepository
                .findAll()
                .map(regionMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
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
            e.printStackTrace()
            log.debug("RES (findByCode) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding region by code.")
        }
    }
}