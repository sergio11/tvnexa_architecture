package com.dreamsoftware.data.iptvorg.datasource.core

import com.dreamsoftware.data.iptvorg.exception.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import java.io.IOException

/**
 * Abstract base class for network data sources that provides methods for handling network requests and exceptions.
 */
abstract class SupportNetworkDataSource {

    companion object {
        /**
         * Constants for some common HTTP response codes.
         */
        const val BAD_REQUEST_CODE: Int = 400
        const val UNAUTHORIZED_CODE: Int = 401
        const val NOT_FOUND_CODE: Int = 404
        const val INTERNAL_SERVER_ERROR_CODE: Int = 500
        const val CONFLICT_ERROR_CODE: Int = 409
        const val FORBIDDEN_CODE: Int = 403
    }

    /**
     * Safely executes a network call, handling exceptions and errors.
     *
     * @param onExecuted A suspend function that performs the network call.
     * @return The result of the network call.
     * @throws NetworkNoInternetException If there is no internet connectivity.
     * @throws NetworkException If a network-related exception occurs.
     * @throws NetworkBadRequestException If a bad request (HTTP 400) is encountered.
     * @throws NetworkUnauthorizedException If an unauthorized request (HTTP 401) is encountered.
     * @throws NetworkForbiddenException If a forbidden request (HTTP 403) is encountered.
     * @throws NetworkNoResultException If no result (HTTP 404) is found.
     * @throws NetworkErrorException If a general network error occurs.
     */
    protected suspend fun <T> safeNetworkCall(onExecuted: suspend () -> T): T {
        try {
            return onExecuted()
        } catch (exception: IOException) {
            throw NetworkNoInternetException()
        } catch (exception: NetworkException) {
            throw exception
        } catch (exception: ResponseException) {
            try {
                throw onApiException(exception)
            } catch (exception: Exception) {
                throw NetworkErrorException(cause = exception)
            }
        } catch (exception: Exception) {
            throw NetworkErrorException(cause = exception)
        }
    }

    /**
     * Maps HTTP error codes to specific exceptions for easier handling.
     *
     * @param responseException The response exception containing HTTP status information.
     * @return An exception that corresponds to the HTTP error code.
     */
    open suspend fun onApiException(responseException: ResponseException): Exception {
        return responseException.response.let {
            when (it.status.value) {
                BAD_REQUEST_CODE -> NetworkBadRequestException(message = it.bodyAsText(), cause = responseException)
                UNAUTHORIZED_CODE -> NetworkUnauthorizedException(message = it.bodyAsText(), cause = responseException)
                FORBIDDEN_CODE -> NetworkForbiddenException(message = it.bodyAsText(), cause = responseException)
                NOT_FOUND_CODE -> NetworkNoResultException(message = it.bodyAsText(), cause = responseException)
                INTERNAL_SERVER_ERROR_CODE -> NetworkErrorException(message = it.bodyAsText(), cause = responseException)
                CONFLICT_ERROR_CODE -> NetworkUnverifiedAccountException(
                    message = it.bodyAsText(),
                    cause = responseException
                )
                else -> NetworkErrorException()
            }
        }
    }
}