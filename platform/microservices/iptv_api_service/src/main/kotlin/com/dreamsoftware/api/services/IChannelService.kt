package com.dreamsoftware.api.services

import com.dreamsoftware.api.dto.ChannelResponseDTO

interface IChannelService {
    @Throws(ChannelServiceException.InternalServerError::class)
    suspend fun findAll(): Iterable<ChannelResponseDTO>
    @Throws(ChannelServiceException.InternalServerError::class, ChannelServiceException.ChannelNotFoundException::class)
    suspend fun findById(channelId: String): ChannelResponseDTO
    suspend fun filterByCategoryAndCountry(category: String?, country: String?): Iterable<ChannelResponseDTO>
}

sealed class ChannelServiceException(message: String) : Exception(message) {
    class ChannelNotFoundException(channelId: String) : ChannelServiceException("Channel with ID '$channelId' not found.")
    class InternalServerError(message: String) : ChannelServiceException("Internal server error: $message")
}