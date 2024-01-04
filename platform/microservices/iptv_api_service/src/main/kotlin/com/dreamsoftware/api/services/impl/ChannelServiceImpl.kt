package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.ChannelResponseDTO
import com.dreamsoftware.api.repository.IChannelRepository
import com.dreamsoftware.api.services.IChannelService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

class ChannelServiceImpl(
    private val channelRepository: IChannelRepository,
    private val channelMapper: ISimpleMapper<ChannelEntity, ChannelResponseDTO>
): IChannelService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(AppException.InternalServerError::class)
    override suspend fun findByCategoryAndCountryPaginated(category: String?, country: String?, offset: Long, limit: Long): List<ChannelResponseDTO> = withContext(Dispatchers.IO) {
        try {
            log.debug("CHS (findByCategoryAndCountryPaginated) category: $category, country: $country, offset: $offset - limit: $limit")
            channelRepository
                .findByCategoryAndCountryPaginated(category, country, offset, limit)
                .map(channelMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("CHS (findByCategoryAndCountryPaginated) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while fetching channels.")
        }
    }

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class
    )
    override suspend fun findById(channelId: String): ChannelResponseDTO = withContext(Dispatchers.IO) {
        try {
            channelRepository.findById(channelId)?.let(channelMapper::map) ?: run {
                throw AppException.NotFoundException.ChannelNotFoundException("Channel with ID '$channelId' not found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("CS (findById) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding channel by ID.")
        }
    }
}