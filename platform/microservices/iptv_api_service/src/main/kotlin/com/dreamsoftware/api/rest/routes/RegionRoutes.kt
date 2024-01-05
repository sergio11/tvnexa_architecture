package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.domain.services.IRegionService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.regionsRoutes() {
    val regionService by inject<IRegionService>()

    route("/regions") {
        // Endpoint to retrieve all regions
        get("/") {
            call.generateSuccessResponse(
                code = 5001,
                message = "Regions retrieved successfully.",
                data = regionService.findAll()
            )
        }

        // Endpoint to retrieve a region by its code
        get("/{regionCode}") {
            with(call) {
                doIfParamExists("regionCode") { regionCode ->
                    generateSuccessResponse(
                        code = 5002,
                        message = "Region found.",
                        data = regionService.findByCode(regionCode)
                    )
                }
            }
        }
    }
}
