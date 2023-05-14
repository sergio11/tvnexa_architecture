package com.dreamsoftware.data.iptvorg.datasource.core

import com.dreamsoftware.data.iptvorg.exception.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.charset.Charset

/**
 * Some HTTP response codes that We could get when making something request
 */
const val BAD_REQUEST_CODE: Int = 400
const val UNAUTHORIZED_CODE: Int = 401
const val NOT_FOUND_CODE: Int = 404
const val INTERNAL_SERVER_ERROR_CODE: Int = 500
const val CONFLICT_ERROR_CODE: Int = 409
const val FORBIDDEN_CODE: Int = 403

abstract class SupportNetworkDataSource {

    /**
     * Wrap for safe Network Call
     * @param onExecuted
     */
    protected suspend fun <T> safeNetworkCall(onExecuted: suspend () -> T): T = withContext(Dispatchers.Default)  {
        try {
            onExecuted()
        } catch (exception: IOException){
            // map interrupted I/O to Network No Internet Exception
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
     * Map HTTP Error codes to exceptions to easy handler
     * @param responseException
     */
    open suspend fun onApiException(responseException: ResponseException): Exception =
        responseException.response.let {
            when(it.status.value) {
                BAD_REQUEST_CODE -> {
                    Charset.forName("UTF-8")
                    NetworkBadRequestException(message = it.bodyAsText(), cause = responseException)
                }
                UNAUTHORIZED_CODE -> {
                    Charset.forName("UTF-8")
                    NetworkUnauthorizedException(message = it.bodyAsText(), cause = responseException)
                }
                FORBIDDEN_CODE -> {
                    Charset.forName("UTF-8")
                    NetworkForbiddenException(message = it.bodyAsText(), cause = responseException)
                }
                NOT_FOUND_CODE -> {
                    Charset.forName("UTF-8")
                    NetworkNoResultException(message = it.bodyAsText(), cause = responseException)
                }
                INTERNAL_SERVER_ERROR_CODE -> {
                    Charset.forName("UTF-8")
                    NetworkErrorException(message = it.bodyAsText(), cause = responseException)
                }
                CONFLICT_ERROR_CODE -> {
                    Charset.forName("UTF-8")
                    NetworkUnverifiedAccountException(message = it.bodyAsText(), cause = responseException)
                }
                else -> NetworkErrorException()
            }
        }
}