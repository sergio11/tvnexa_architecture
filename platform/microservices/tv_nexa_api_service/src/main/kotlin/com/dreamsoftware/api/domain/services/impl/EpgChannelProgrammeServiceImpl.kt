package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IChannelRepository
import com.dreamsoftware.api.domain.repository.IEpgChannelProgrammeRepository
import com.dreamsoftware.api.domain.services.IEpgChannelProgrammeService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.response.EpgChannelProgrammeResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.time.LocalDateTime

/**
 * Implementation of the [IEpgChannelProgrammeService] interface responsible for providing EPG (Electronic Program Guide)
 * information related operations.
 *
 * @property epgChannelProgrammeRepository The repository responsible for EPG-related data operations.
 * @property channelRepository The repository responsible for channel-related data operations.
 * @property epgChannelProgrammeMapper The mapper used to map EPG channel program entities to response DTOs.
 */
internal class EpgChannelProgrammeServiceImpl(
    private val epgChannelProgrammeRepository: IEpgChannelProgrammeRepository,
    private val channelRepository: IChannelRepository,
    private val epgChannelProgrammeMapper: ISimpleMapper<EpgChannelProgrammeEntity, EpgChannelProgrammeResponseDTO>
) : SupportService(), IEpgChannelProgrammeService {

    /**
     * Retrieves EPG (Electronic Program Guide) programs for a specific channel within a given date range.
     *
     * @param channelId The unique identifier of the channel.
     * @param startAt The start date and time of the date range.
     * @param endAt The end date and time of the date range.
     * @return A list of [EpgChannelProgrammeResponseDTO] objects representing the EPG programs for the channel.
     * @throws AppException.InternalServerError if an error occurs during the data retrieval process.
     * @throws AppException.NotFoundException.EpgChannelNotFoundException if the specified channel is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.EpgChannelNotFoundException::class
    )
    override suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): List<EpgChannelProgrammeResponseDTO> =
        safeCall(errorMessage = "An error occurred while finding EPG information by channel id and date range.") {
            // Fetch EPG programs from the repository, map them, and return as a list
            epgChannelProgrammeRepository
                .findByChannelIdAndDateRange(channelId, startAt, endAt)
                .map(epgChannelProgrammeMapper::map)
        }

    /**
     * Retrieves EPG (Electronic Program Guide) programs for multiple channels within a given country and date range.
     *
     * @param countryCode The ISO 3166-1 alpha-2 country code.
     * @param startAt The start date and time of the date range.
     * @param endAt The end date and time of the date range.
     * @return A map where the keys are channel IDs and the values are lists of [EpgChannelProgrammeResponseDTO] objects
     * representing the EPG programs for each channel.
     * @throws AppException.InternalServerError if an error occurs during the data retrieval process.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findByCountryAndDate(
        countryCode: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Map<String, List<EpgChannelProgrammeResponseDTO>> =
        safeCall(errorMessage = "An error occurred while finding EPG information by country and date.") {
            // Fetch a list of channels for the given country
            val channels = channelRepository.filterByCountry(countryCode)
            // Use async/await to fetch programs for multiple channels concurrently
            channels.map { channel ->
                async {
                    // Fetch EPG programs for a channel, map them, and return as a list
                    channel.channelId to epgChannelProgrammeRepository.findByChannelIdAndDateRange(
                        channel.channelId, startAt, endAt
                    ).map(epgChannelProgrammeMapper::map)
                }
            }.awaitAll().toMap()
        }
}