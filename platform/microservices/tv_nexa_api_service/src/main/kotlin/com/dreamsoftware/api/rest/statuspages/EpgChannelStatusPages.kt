package com.dreamsoftware.api.rest.statuspages

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.model.toErrorResponseDTO
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureEpgChannelStatusPages() {
    exception<AppException.NotFoundException.EpgChannelNotFoundException> { call, _ ->
        call.respond(
            HttpStatusCode.NotFound,
            ErrorType.EPG_CHANNEL_NOT_FOUND.toErrorResponseDTO()
        )
    }
}