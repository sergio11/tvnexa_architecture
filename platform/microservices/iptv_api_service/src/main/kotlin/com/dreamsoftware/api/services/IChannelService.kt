package com.dreamsoftware.api.services

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.ChannelResponseDTO

/**
 * Interface for managing channels.
 */
interface IChannelService {

    /**
     * Retrieves a paginated list of channels.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channels to be retrieved in a single request.
     * @return List of ChannelResponseDTO containing a paginated list of channels.
     * @throws AppException.InternalServerError if there's an internal server error during retrieval.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findPaginated(offset: Long, limit: Long): List<ChannelResponseDTO>

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
    suspend fun findById(channelId: String): ChannelResponseDTO

    /**
     * Filters channels by category and country.
     *
     * @param category The category by which to filter the channels.
     * @param country The country by which to filter the channels.
     * @return Iterable of ChannelResponseDTO containing filtered channels.
     */
    suspend fun filterByCategoryAndCountry(category: String?, country: String?): List<ChannelResponseDTO>
}
