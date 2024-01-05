package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.EpgChannelProgrammeResponseDTO
import com.dreamsoftware.api.domain.repository.IChannelRepository
import com.dreamsoftware.api.domain.repository.IEpgChannelProgrammeRepository
import com.dreamsoftware.api.domain.services.IEpgChannelProgrammeService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class EpgChannelProgrammeServiceImpl(
    private val epgChannelProgrammeRepository: IEpgChannelProgrammeRepository,
    private val channelRepository: IChannelRepository,
    private val epgChannelProgrammeMapper: ISimpleMapper<EpgChannelProgrammeEntity, EpgChannelProgrammeResponseDTO>
) : IEpgChannelProgrammeService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.EpgChannelNotFoundException::class
    )
    override suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): List<EpgChannelProgrammeResponseDTO> = withContext(Dispatchers.IO) {
        try {
            // Fetch EPG programs from the repository, map them, and return as a list
            epgChannelProgrammeRepository
                .findByChannelIdAndDateRange(channelId, startAt, endAt)
                .map(epgChannelProgrammeMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("EPGS (findByChannelIdAndDateRange) An exception occurred: ${e.message ?: "Unknown error"}")
            // Handle exceptions and throw a custom service exception
            throw AppException.InternalServerError("An error occurred while finding EPG information by channel id and date range.")
        }
    }

    @Throws(AppException.InternalServerError::class)
    override suspend fun findByCountryAndDate(
        countryCode: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Map<String, List<EpgChannelProgrammeResponseDTO>> = withContext(Dispatchers.IO) {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("EPGS (findByChannelIdAndDateRange) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding EPG information by country and date.")
        }
    }
}