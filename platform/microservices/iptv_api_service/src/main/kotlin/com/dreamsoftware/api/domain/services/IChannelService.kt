package com.dreamsoftware.api.domain.services

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.ChannelDetailResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO

/**
 * Interface for managing channels.
 */
interface IChannelService {

    /**
     * Retrieves a paginated list of channels.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channels to be retrieved in a single request.
     * @param category The category by which to filter the channels.
     * @param country The country by which to filter the channels.
     * @return List of ChannelResponseDTO containing a paginated list of channels.
     * @throws AppException.InternalServerError if there's an internal server error during retrieval.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findByCategoryAndCountryPaginated(category: String?, country: String?, offset: Long, limit: Long): List<SimpleChannelResponseDTO>

    /**
     * Retrieves a channel by its ID.
     *
     * @param channelId The ID of the channel to retrieve.
     * @return ChannelResponseDTO containing the channel details.
     * @throws AppException.InternalServerError if there's an internal server error.
     * @throws AppException.NotFoundException.ChannelNotFoundException if the channel is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class
    )
    suspend fun findById(channelId: String): ChannelDetailResponseDTO
}
