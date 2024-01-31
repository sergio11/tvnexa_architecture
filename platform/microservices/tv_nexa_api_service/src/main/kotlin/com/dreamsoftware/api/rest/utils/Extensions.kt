package com.dreamsoftware.api.rest.utils

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.domain.model.toErrorResponseDTO
import com.dreamsoftware.api.rest.dto.response.ApiResponseDTO
import com.dreamsoftware.core.toUUID
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

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
 * Extension function for [ApplicationCall] to retrieve a UUID parameter from the call's parameters map.
 *
 * @param paramName The name of the UUID parameter to retrieve.
 * @return The UUID value associated with the specified parameter name.
 * @throws BadRequestException if the parameter is not present or if it cannot be converted to a valid UUID.
 */
fun ApplicationCall.getUUIDParamOrThrow(paramName: String): UUID =
    parameters[paramName]?.let {
        runCatching { it.toUUID() }.getOrNull() ?: throw BadRequestException("Invalid UUID")
    } ?: throw BadRequestException("UUID not provided")


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

/**
 * Extension function to retrieve a String property from the application configuration.
 *
 * This function is an extension on the `ApplicationConfig` class and is used to obtain a String property
 * identified by the specified `name` from the application configuration.
 *
 * @param name The name of the property to retrieve.
 * @return Returns the String value of the specified property.
 */
fun ApplicationConfig.getStringProperty(name: String): String = property(name).getString()


/**
 * Extension function for [ApplicationCall] to fetch the authenticated user's UUID from a JWT token.
 *
 * @return The [UUID] of the authenticated user if present, or null if the user is not authenticated or the JWT subject is not a valid UUID.
 */
fun ApplicationCall.fetchAuthUserUuidOrThrow(): UUID = authentication.principal<JWTPrincipal>()?.subject?.let(UUID::fromString)
    ?: throw BadRequestException("Authorization header can not be found")