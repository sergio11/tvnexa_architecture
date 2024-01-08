package com.dreamsoftware.api.domain.model

import com.dreamsoftware.api.rest.dto.ErrorResponseDTO

enum class ErrorType(val code: Int, val message: String) {
    CATEGORY_NOT_FOUND(100, "The requested category was not found in the system."),
    REGION_NOT_FOUND(101, "The requested region was not found in the system."),
    CHANNEL_NOT_FOUND(102, "The requested channel was not found in the system."),
    COUNTRY_NOT_FOUND(103, "The requested country was not found in the system."),
    REQUEST_VALIDATION_FAILED(104, "The provided data did not pass the validation checks."),
    EPG_CHANNEL_NOT_FOUND(105, "The requested EPG channel was not found in the system."),
    INTERNAL_SERVER_ERROR(107, "An internal server error occurred. Please try again later."),
    SUBDIVISION_NOT_FOUND(110, "The requested subdivision was not found in the system."),
    BAD_REQUEST(109, "The request sent was invalid or incomplete. Please review and ensure all required data is provided."),
    UNAUTHORIZED_CLIENT_EXCEPTION(108, "Operation denied. You do not have the necessary permissions to perform this action.")
}

fun ErrorType.toErrorResponseDTO() = ErrorResponseDTO(
    name, code, message
)