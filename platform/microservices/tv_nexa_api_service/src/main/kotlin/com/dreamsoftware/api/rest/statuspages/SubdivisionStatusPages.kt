package com.dreamsoftware.api.rest.statuspages

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.model.toErrorResponseDTO
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureSubdivisionStatusPages() {
    exception<AppException.NotFoundException.SubdivisionNotFoundException> { call, _ ->
        call.respond(
            HttpStatusCode.NotFound,
            ErrorType.SUBDIVISION_NOT_FOUND.toErrorResponseDTO()
        )
    }
}