package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.dto.RegionResponseDTO
import com.dreamsoftware.api.repository.IRegionRepository
import com.dreamsoftware.api.services.IRegionService
import com.dreamsoftware.api.services.RegionServiceException
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.RegionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegionServiceImpl(
    private val regionRepository: IRegionRepository,
    private val regionMapper: IMapper<RegionEntity, RegionResponseDTO>
) : IRegionService {

    @Throws(RegionServiceException.InternalServerError::class)
    override suspend fun findAll(): Iterable<RegionResponseDTO> = withContext(Dispatchers.IO) {
        try {
            val regions = regionRepository.findAll()
            regions.map(regionMapper::map)
        } catch (e: Exception) {
            throw RegionServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    @Throws(RegionServiceException.InternalServerError::class, RegionServiceException.RegionNotFoundException::class)
    override suspend fun findByCode(code: String): RegionResponseDTO = withContext(Dispatchers.IO) {
        try {
            val region = regionRepository.findByCode(code)
            if (region != null) {
                regionMapper.map(region)
            } else {
                throw RegionServiceException.RegionNotFoundException(code)
            }
        } catch (e: Exception) {
            throw RegionServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}