package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.RegionResponseDTO
import com.dreamsoftware.api.repository.IRegionRepository
import com.dreamsoftware.api.services.IRegionService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.RegionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegionServiceImpl(
    private val regionRepository: IRegionRepository,
    private val regionMapper: ISimpleMapper<RegionEntity, RegionResponseDTO>
) : IRegionService {

    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): Iterable<RegionResponseDTO> = withContext(Dispatchers.IO) {
        try {
            val regions = regionRepository.findAll()
            regions.map(regionMapper::map)
        } catch (e: Exception) {
            throw AppException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.RegionNotFoundException::class
    )
    override suspend fun findByCode(code: String): RegionResponseDTO = withContext(Dispatchers.IO) {
        try {
            val region = regionRepository.findByCode(code)
            if (region != null) {
                regionMapper.map(region)
            } else {
                throw AppException.NotFoundException.RegionNotFoundException(code)
            }
        } catch (e: Exception) {
            throw AppException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}