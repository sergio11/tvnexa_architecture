package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.rest.utils.generateErrorResponse
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.utils.getLocalDateTimeQueryParamOrNull
import com.dreamsoftware.api.domain.services.IEpgChannelProgrammeService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Route.epgChannelProgrammeRoutes() {
    val epgChannelProgrammeService by inject<IEpgChannelProgrammeService>()

    route("/epg") {
        // Endpoint to retrieve programs by channel
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

        // Endpoint to retrieve programs by country
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