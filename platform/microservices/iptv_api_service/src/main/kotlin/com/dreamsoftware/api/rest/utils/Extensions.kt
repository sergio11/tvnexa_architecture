package com.dreamsoftware.api.rest.utils

import com.dreamsoftware.api.model.ErrorType
import com.dreamsoftware.api.model.toErrorResponseDTO
import com.dreamsoftware.api.rest.dto.ApiResponseDTO
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Generates a success response for the API call.
 *
 * @param code The status code for the response.
 * @param message A message describing the success state.
 * @param data The data to be included in the response.
 * @return Unit
 */
suspend inline fun <reified T> ApplicationCall.generateSuccessResponse(code: Int, message: String, data: T) {
    respond(ApiResponseDTO(code, message, data))
}

/**
 * Generates an error response for the API call based on the provided error type.
 *
 * @param errorType The type of error to generate the response for.
 * @return Unit
 */
suspend fun ApplicationCall.generateErrorResponse(errorType: ErrorType) {
    respond(errorType.toErrorResponseDTO())
}

/**
 * Executes a block of code if the specified parameter exists, otherwise generates a 'BAD_REQUEST' error response.
 *
 * @param paramName The name of the parameter to check.
 * @param block The block of code to execute if the parameter exists.
 * @return Unit
 */
suspend fun ApplicationCall.doIfParamExists(paramName: String, block: suspend (param: String) -> Unit) {
    parameters[paramName]?.let { block(it) } ?: run {
        generateErrorResponse(ErrorType.BAD_REQUEST)
    }
}

/**
 * Retrieves a Long parameter or returns a default value if the parameter is not present or invalid.
 *
 * @param paramName The name of the Long parameter to retrieve.
 * @param defaultValue The default value to return if the parameter is not present or invalid.
 * @return Long value obtained from the parameter or the default value.
 */
fun ApplicationCall.getLongParamOrDefault(paramName: String, defaultValue: Long) =
    parameters[paramName]?.toLongOrNull() ?: defaultValue


/**
 * Retrieves a LocalDateTime parameter from the query parameters or returns null if the parameter is absent or invalid.
 *
 * @param paramName The name of the LocalDateTime parameter to retrieve.
 * @return LocalDateTime value obtained from the query parameter or null if absent or invalid.
 */
fun ApplicationCall.getLocalDateTimeQueryParamOrNull(paramName: String): LocalDateTime? =
    request.queryParameters[paramName]?.let {
        runCatching {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
        }.getOrNull()
    }