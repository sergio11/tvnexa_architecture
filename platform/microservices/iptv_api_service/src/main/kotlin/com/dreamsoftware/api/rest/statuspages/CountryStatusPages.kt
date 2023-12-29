package com.dreamsoftware.api.rest.statuspages

import com.dreamsoftware.api.model.ErrorType
import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.model.toErrorResponseDTO
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.configureCountryStatusPages() {
    exception<AppException.NotFoundException.CountryNotFoundException> { call, _ ->
        call.respond(
            HttpStatusCode.NotFound,
            ErrorType.COUNTRY_NOT_FOUND.toErrorResponseDTO()
        )
    }
}