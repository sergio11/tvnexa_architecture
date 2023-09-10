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

    route("/epg/channel-programmes") {
        get("/{channelId}") {
            val channelId = call.parameters["channelId"] ?: return@get call.respond(HttpStatusCode.BadRequest, "The 'channelId' param is missing")
            val startAtStr = call.request.queryParameters["startAt"]
            val endAtStr = call.request.queryParameters["endAt"]

            if (startAtStr == null || endAtStr == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "You must provide the 'startAt' and 'endAt' params")
            }
            try {
                val startAt = LocalDateTime.parse(startAtStr, DateTimeFormatter.ISO_DATE_TIME)
                val endAt = LocalDateTime.parse(endAtStr, DateTimeFormatter.ISO_DATE_TIME)
                val programmes = epgChannelProgrammeService.findByChannelIdAndDateRange(channelId, startAt, endAt)
                call.respond(HttpStatusCode.OK, programmes)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Wrong Date format: ${e.message}")
            }
        }
    }
}