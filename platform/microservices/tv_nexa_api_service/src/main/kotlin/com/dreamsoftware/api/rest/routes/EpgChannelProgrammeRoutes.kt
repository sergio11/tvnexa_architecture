package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.rest.utils.generateErrorResponse
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.utils.getLocalDateTimeQueryParamOrNull
import com.dreamsoftware.api.rest.controllers.IEpgChannelProgrammeController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

/**
 * Class representing the routes related to Electronic Program Guide (EPG) channel programmes in the application.
 * These routes include functionalities such as retrieving EPG data based on channel ID and date range,
 * as well as retrieving EPG data based on country code and date range.
 *
 * @property epgChannelProgrammeService An instance of the [IEpgChannelProgrammeController] interface
 * for handling EPG channel programme-related operations.
 */
fun Route.epgChannelProgrammeRoutes() {
    val epgChannelProgrammeService by inject<IEpgChannelProgrammeController>()

    /**
     * Defines the routes under the "/epg" endpoint for EPG channel programme-related operations.
     */
    route("/epg") {

        /**
         * Endpoint for retrieving EPG data based on channel ID and date range.
         * Accepts GET requests to "/epg/channel-programmes/{channelId}" and retrieves EPG data
         * using the [epgChannelProgrammeService.findByChannelIdAndDateRange] method.
         * Generates a success response with a code of 4000, a message indicating successful EPG data retrieval by channel,
         * and data containing the list of EPG programmes.
         *
         * Path Parameter:
         * - channelId: The ID or code of the channel for which EPG data is to be retrieved.
         * Query Parameters:
         * - startAt: The start date and time of the date range for EPG data retrieval.
         * - endAt: The end date and time of the date range for EPG data retrieval.
         */
        get("/channel-programmes/{channelId}") {
            with(call) {
                handleEpgProgrammesRequest { idOrCode, startAt, endAt ->
                    val programmes = epgChannelProgrammeService.findByChannelIdAndDateRange(idOrCode, startAt, endAt)
                    generateSuccessResponse(
                        code = 4000,
                        message = "EPG data by channel retrieved successfully.",
                        data = programmes
                    )
                }
            }
        }

        /**
         * Endpoint for retrieving EPG data based on country code and date range.
         * Accepts GET requests to "/epg/country-programmes/{countryCode}" and retrieves EPG data
         * using the [epgChannelProgrammeService.findByCountryAndDate] method.
         * Generates a success response with a code of 4001, a message indicating successful EPG data retrieval by country,
         * and data containing the list of EPG programmes.
         *
         * Path Parameter:
         * - countryCode: The code of the country for which EPG data is to be retrieved.
         * Query Parameters:
         * - startAt: The start date and time of the date range for EPG data retrieval.
         * - endAt: The end date and time of the date range for EPG data retrieval.
         */
        get("/country-programmes/{countryCode}") {
            with(call) {
                handleEpgProgrammesRequest { idOrCode, startAt, endAt ->
                    val programmes = epgChannelProgrammeService.findByCountryAndDate(idOrCode, startAt, endAt)
                    generateSuccessResponse(
                        code = 4001,
                        message = "EPG data by country retrieved successfully.",
                        data = programmes
                    )
                }
            }
        }
    }
}

/**
 * Handles the EPG (Electronic Program Guide) program request.
 *
 * @param fetchProgrammes A suspending function that retrieves program data.
 *                        Takes channelId/countryCode, startAt, and endAt as parameters.
 * @return Unit
 */
suspend fun ApplicationCall.handleEpgProgrammesRequest(
    onDoWork: suspend (String, LocalDateTime, LocalDateTime) -> Unit
) {
    val idOrCode = parameters["channelId"] ?: parameters["countryCode"]
    val startAt = getLocalDateTimeQueryParamOrNull("startAt")
    val endAt = getLocalDateTimeQueryParamOrNull("endAt")
    if (idOrCode == null || startAt == null || endAt == null) {
        generateErrorResponse(ErrorType.BAD_REQUEST)
    } else {
        onDoWork(idOrCode, startAt, endAt)
    }
}