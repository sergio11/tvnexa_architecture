package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.dto.ChannelResponseDTO
import com.dreamsoftware.api.repository.IChannelRepository
import com.dreamsoftware.api.services.ChannelServiceException
import com.dreamsoftware.api.services.IChannelService
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelServiceImpl(
    private val channelRepository: IChannelRepository,
    private val channelMapper: IMapper<ChannelEntity, ChannelResponseDTO>
): IChannelService {

    @Throws(ChannelServiceException.InternalServerError::class)
    override suspend fun findAll(): Iterable<ChannelResponseDTO> = withContext(Dispatchers.IO) {
        try {
            val channels = channelRepository.findAll()
            channels.map(channelMapper::map)
        } catch (e: Exception) {
            throw ChannelServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    @Throws(ChannelServiceException.InternalServerError::class, ChannelServiceException.ChannelNotFoundException::class)
    override suspend fun findById(channelId: String): ChannelResponseDTO = withContext(Dispatchers.IO) {
        try {
            val channel = channelRepository.findById(channelId)
            if (channel != null) {
                channelMapper.map(channel)
            } else {
                throw ChannelServiceException.ChannelNotFoundException(channelId)
            }
        } catch (e: Exception) {
            throw ChannelServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    override suspend fun filterByCategoryAndCountry(category: String?, country: String?): Iterable<ChannelResponseDTO> =
        withContext(Dispatchers.IO) {
            try {
                val filteredChannels = channelRepository.filterByCategoryAndCountry(category, country)
                filteredChannels.map(channelMapper::map)
            } catch (e: Exception) {
                throw ChannelServiceException.InternalServerError(e.message ?: "Unknown error")
            }
        }
}