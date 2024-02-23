package com.dreamsoftware.api.rest.controllers.impl.core

import com.dreamsoftware.api.domain.model.exceptions.AppException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

/**
 * Base class providing support functions for services.
 */
abstract class SupportController {
    // Logger for logging service-related information
    protected val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Executes the specified block of code within a safe context, handling exceptions and converting them to
     * [AppException.InternalServerError].
     *
     * @param block The block of code to be executed.
     * @param errorMessage The error message to be logged in case of an exception.
     * @throws AppException.InternalServerError if an exception occurs during the execution of the block.
     */
    suspend fun <T>safeCall(
        errorMessage: String,
        block: suspend CoroutineScope.() -> T
    ) = withContext(Dispatchers.IO) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("$errorMessage: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError(errorMessage)
            } else {
                e
            }
        }
    }
}