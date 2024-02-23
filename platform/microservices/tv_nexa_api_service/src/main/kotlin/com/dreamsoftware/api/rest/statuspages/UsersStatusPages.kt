package com.dreamsoftware.api.rest.statuspages

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.model.toErrorResponseDTO
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureUsersStatusPages() {
    exception<AppException.NotFoundException.UserNotFoundException> { call, _ ->
        call.respond(
            HttpStatusCode.NotFound,
            ErrorType.USER_NOT_FOUND.toErrorResponseDTO()
        )
    }
    exception<AppException.NotFoundException.UserNotAllowedException> { call, _ ->
        call.respond(
            HttpStatusCode.Forbidden,
            ErrorType.USER_NOT_ALLOWED.toErrorResponseDTO()
        )
    }
    exception<AppException.InvalidCredentialsException> { call, _ ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorType.INVALID_CREDENTIALS.toErrorResponseDTO()
        )
    }
    exception<AppException.UserAlreadyExistsException> { call, _ ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorType.USER_ALREADY_EXISTS.toErrorResponseDTO()
        )
    }
    exception<AppException.NotFoundException.ProfileNotFoundException>() { call, _ ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorType.PROFILE_NOT_FOUND.toErrorResponseDTO()
        )
    }
}