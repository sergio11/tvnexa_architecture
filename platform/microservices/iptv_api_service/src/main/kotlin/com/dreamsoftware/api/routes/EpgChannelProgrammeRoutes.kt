package com.dreamsoftware.api.routes

import com.dreamsoftware.api.services.IEpgChannelProgrammeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Routing.epgChannelProgrammeRoutes() {
    val epgChannelProgrammeService by inject<IEpgChannelProgrammeService>()

    route("/epg") {
        // Endpoint to retrieve programs by channel
        get("/channel-programmes/{channelId}") {
            handleProgrammesRequest(call, epgChannelProgrammeService::findByChannelIdAndDateRange)
        }

        // Endpoint to retrieve programs by country
        get("/country-programmes/{countryCode}") {
            handleProgrammesRequest(call, epgChannelProgrammeService::findByCountryAndDate)
        }
    }
}

suspend inline fun handleProgrammesRequest(
    call: ApplicationCall,
    fetchProgrammes: suspend (String, LocalDateTime, LocalDateTime) -> Any
) {
    val idOrCode = call.parameters["channelId"] ?: call.parameters["countryCode"]
    val startAtStr = call.request.queryParameters["startAt"]
    val endAtStr = call.request.queryParameters["endAt"]

    if (idOrCode == null || startAtStr == null || endAtStr == null) {
        call.respond(HttpStatusCode.BadRequest, "Missing parameters")
        return
    }

    try {
        val startAt = LocalDateTime.parse(startAtStr, DateTimeFormatter.ISO_DATE_TIME)
        val endAt = LocalDateTime.parse(endAtStr, DateTimeFormatter.ISO_DATE_TIME)
        val programmes = fetchProgrammes(idOrCode, startAt, endAt)
        call.respond(HttpStatusCode.OK, programmes)
    } catch (e: Exception) {
        call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
    }
}