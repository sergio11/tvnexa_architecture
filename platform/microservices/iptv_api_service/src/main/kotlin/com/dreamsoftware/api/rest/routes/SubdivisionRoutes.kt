package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.domain.services.ISubdivisionService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.subdivisionRoutes() {
    val subdivisionService by inject<ISubdivisionService>()

    route("/subdivisions") {
        // Endpoint to retrieve all subdivisions
        get("/") {
            call.generateSuccessResponse(
                code = 6001,
                message = "Subdivisions retrieved successfully.",
                data = subdivisionService.findAll()
            )
        }

        // Endpoint to retrieve a subdivision by its code
        get("/{subdivisionCode}") {
            with(call) {
                doIfParamExists("subdivisionCode") { regionCode ->
                    generateSuccessResponse(
                        code = 6002,
                        message = "Subdivision found.",
                        data = subdivisionService.findByCode(regionCode)
                    )
                }
            }
        }
    }
}
