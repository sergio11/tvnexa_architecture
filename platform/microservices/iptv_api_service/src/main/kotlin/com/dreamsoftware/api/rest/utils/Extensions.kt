package com.dreamsoftware.api.rest.utils

import com.dreamsoftware.api.model.ErrorType
import com.dreamsoftware.api.model.toErrorResponseDTO
import com.dreamsoftware.api.rest.dto.ApiResponseDTO
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend inline fun <reified T> ApplicationCall.generateSuccessResponse(code: Int, message: String, data: T) {
    respond(ApiResponseDTO(code, message, data))
}

suspend fun ApplicationCall.generateErrorResponse(errorType: ErrorType) {
    respond(errorType.toErrorResponseDTO())
}