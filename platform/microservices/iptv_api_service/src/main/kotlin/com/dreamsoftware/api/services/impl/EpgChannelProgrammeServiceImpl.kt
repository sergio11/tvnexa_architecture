package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.EpgChannelProgrammeResponseDTO
import com.dreamsoftware.api.repository.IChannelRepository
import com.dreamsoftware.api.repository.IEpgChannelProgrammeRepository
import com.dreamsoftware.api.services.IEpgChannelProgrammeService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class EpgChannelProgrammeServiceImpl(
    private val epgChannelProgrammeRepository: IEpgChannelProgrammeRepository,
    private val channelRepository: IChannelRepository,
    private val epgChannelProgrammeMapper: ISimpleMapper<EpgChannelProgrammeEntity, EpgChannelProgrammeResponseDTO>
) : IEpgChannelProgrammeService {

    // Retrieve EPG programs by channel ID and date range
    override suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Iterable<EpgChannelProgrammeResponseDTO> = withContext(Dispatchers.IO) {
        try {
            // Fetch EPG programs from the repository, map them, and return as a list
            return@withContext epgChannelProgrammeRepository
                .findByChannelIdAndDateRange(channelId, startAt, endAt)
                .map(epgChannelProgrammeMapper::map)
        } catch (e: Exception) {
            // Handle exceptions and throw a custom service exception
            throw AppException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    // Retrieve EPG programs for multiple channels in a country and date range
    override suspend fun findByCountryAndDate(
        countryCode: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Map<String, List<EpgChannelProgrammeResponseDTO>> = withContext(Dispatchers.IO) {
        try {
            // Fetch a list of channels for the given country
            val channels = channelRepository.filterByCountry(countryCode)
            // Use async/await to fetch programs for multiple channels concurrently
            val deferredProgrammes = channels.map { channel ->
                async {
                    // Fetch EPG programs for a channel, map them, and return as a list
                    channel.channelId to epgChannelProgrammeRepository.findByChannelIdAndDateRange(
                        channel.channelId, startAt, endAt
                    ).map(epgChannelProgrammeMapper::map)
                }
            }
            // Convert the list of deferred results to a map of channel IDs and their respective programs
            return@withContext deferredProgrammes.awaitAll().toMap()
        } catch (e: Exception) {
            // Handle exceptions and throw a custom service exception
            throw AppException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}