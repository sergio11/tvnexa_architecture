package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IChannelRepository
import com.dreamsoftware.api.domain.services.IChannelService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.response.ChannelDetailResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.ChannelDetailEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * Implementation of the IChannelService interface responsible for managing channel-related operations.
 *
 * @property channelRepository The repository responsible for channel-related data operations.
 * @property channelDetailMapper The mapper used to map ChannelDetailEntity objects to ChannelDetailResponseDTO objects.
 * @property simpleChannelMapper The mapper used to map SimpleChannelEntity objects to SimpleChannelResponseDTO objects.
 */
internal class ChannelServiceImpl(
    private val channelRepository: IChannelRepository,
    private val channelDetailMapper: ISimpleMapper<ChannelDetailEntity, ChannelDetailResponseDTO>,
    private val simpleChannelMapper: ISimpleMapper<SimpleChannelEntity, SimpleChannelResponseDTO>
): SupportService(), IChannelService {

    /**
     * Retrieves a list of simple channel responses by category, country, offset, and limit.
     *
     * @param category The category for channel filtering.
     * @param country The country for channel filtering.
     * @param offset The starting index for paginated results.
     * @param limit The maximum number of channels to retrieve.
     * @return A list of SimpleChannelResponseDTO objects matching the criteria.
     * @throws AppException.InternalServerError if an internal server error occurs while fetching channels.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findByCategoryAndCountryPaginated(category: String?, country: String?, offset: Long, limit: Long): List<SimpleChannelResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching channels.") {
            log.debug("CHS (findByCategoryAndCountryPaginated) category: $category, country: $country, offset: $offset - limit: $limit")
            channelRepository
                .findByCategoryAndCountryPaginated(category, country, offset, limit)
                .map(simpleChannelMapper::map)
        }

    /**
     * Retrieves a channel's detailed information by ID.
     *
     * @param channelId The ID of the channel to retrieve.
     * @return The detailed information of the channel.
     * @throws AppException.InternalServerError if an internal server error occurs while finding the channel by ID.
     * @throws AppException.NotFoundException.ChannelNotFoundException if the channel with the given ID is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class
    )
    override suspend fun findById(channelId: String): ChannelDetailResponseDTO =
        safeCall(errorMessage = "An error occurred while finding channel by ID.") {
            channelRepository.findById(channelId)?.let(channelDetailMapper::map) ?:
                throw AppException.NotFoundException.ChannelNotFoundException("Channel with ID '$channelId' not found.")
        }

    /**
     * Searches for channels whose names contain the specified term in a case-insensitive manner.
     *
     * @param term The search term to match against channel names.
     * @return A list of [SimpleChannelResponseDTO] representing the channels found.
     * @throws AppException.InternalServerError if an internal server error occurs during the search.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findByNameLike(term: String): List<SimpleChannelResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching channels.") {
            log.debug("CHS (findByNameLike) term: $term")
            channelRepository
                .findByNameLike(term)
                .map(simpleChannelMapper::map)
        }
}