package com.dreamsoftware.api.domain.model.exceptions

sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    sealed class NotFoundException(message: String? = null, cause: Throwable? = null) : AppException(message, cause) {
        class CategoryNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
        class ChannelNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
        class CountryNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
        class EpgChannelNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
        class RegionNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
        class SubdivisionNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
        class UserNotFoundException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
    }
    class InvalidCredentialsException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
    class UserAlreadyExistsException(message: String? = null, cause: Throwable? = null) : NotFoundException(message, cause)
    class InternalServerError(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
}