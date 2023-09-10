package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.dto.EpgChannelProgrammeResponseDTO
import com.dreamsoftware.api.repository.IEpgChannelProgrammeRepository
import com.dreamsoftware.api.services.CountryServiceException
import com.dreamsoftware.api.services.EpgChannelProgrammeServiceException
import com.dreamsoftware.api.services.IEpgChannelProgrammeService
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class EpgChannelProgrammeServiceImpl(
    private val epgChannelProgrammeRepository: IEpgChannelProgrammeRepository,
    private val epgChannelProgrammeMapper: IMapper<EpgChannelProgrammeEntity, EpgChannelProgrammeResponseDTO>
): IEpgChannelProgrammeService {

    @Throws(EpgChannelProgrammeServiceException.InternalServerError::class, EpgChannelProgrammeServiceException.EpgChannelNotFoundException::class)
    override suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Iterable<EpgChannelProgrammeResponseDTO> = withContext(Dispatchers.IO) {
        try {
            epgChannelProgrammeRepository.findByChannelIdAndDateRange(channelId, startAt, endAt).map(epgChannelProgrammeMapper::map)
        } catch (e: Exception) {
            throw CountryServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}