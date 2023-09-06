package com.dreamsoftware.api.routes

import com.dreamsoftware.api.services.IRegionService
import com.dreamsoftware.api.services.RegionServiceException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Routing.regionsRoutes() {
    val regionService by inject<IRegionService>()

    route("/regions") {
        // Endpoint to retrieve all regions
        get("/") {
            handleRegionExceptions {
                val regions = regionService.findAll()
                call.respond(regions)
            }
        }

        // Endpoint to retrieve a region by its code
        get("/{regionCode}") {
            val regionCode = call.parameters["regionCode"]
            if (regionCode != null) {
                handleRegionExceptions {
                    val region = regionService.findByCode(regionCode)
                    call.respond(region)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid region code")
            }
        }
    }
}

// Custom exception handling function for regions
suspend fun PipelineContext<*, ApplicationCall>.handleRegionExceptions(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: RegionServiceException.InternalServerError) {
        // Internal server error exception for regions
        call.respond(HttpStatusCode.InternalServerError, "Internal server error: ${e.message}")
    } catch (e: RegionServiceException.RegionNotFoundException) {
        // Region not found exception
        call.respond(HttpStatusCode.NotFound, "Region not found: ${e.message}")
    }
}
