package com.dreamsoftware.api.rest.statuspages

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.model.toErrorResponseDTO
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureCommonStatusPages() {
    exception<RequestValidationException> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorType.REQUEST_VALIDATION_FAILED.toErrorResponseDTO().copy(details = cause.reasons.joinToString()))
    }
    exception<BadRequestException> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorType.REQUEST_VALIDATION_FAILED.toErrorResponseDTO().copy(details = cause.message))
    }
    exception<AppException.InternalServerError> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorType.INTERNAL_SERVER_ERROR.toErrorResponseDTO().copy(details = cause.message))
    }
    exception<AppException.JwtAuthenticationException> { call, cause ->
        call.respond(
            HttpStatusCode.Forbidden,
            ErrorType.UNAUTHORIZED_CLIENT_EXCEPTION.toErrorResponseDTO().copy(details = cause.message))
    }
}