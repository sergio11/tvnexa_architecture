package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.services.IRegionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.regionsRoutes() {
    val regionService by inject<IRegionService>()

    route("/regions") {
        // Endpoint to retrieve all regions
        get("/") {
            val regions = regionService.findAll()
            call.respond(regions)
        }

        // Endpoint to retrieve a region by its code
        get("/{regionCode}") {
            with(call) {
                parameters["regionCode"]?.let { regionCode ->
                    respond(regionService.findByCode(regionCode))
                } ?: run {
                    respond(HttpStatusCode.BadRequest, "Invalid region code")
                }
            }
        }
    }
}
