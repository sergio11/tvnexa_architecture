package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.services.ICountryService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.countryRoutes() {
    val countryService by inject<ICountryService>()

    route("/countries") {
        // Endpoint to retrieve all countries
        get("/") {
            call.generateSuccessResponse(
                code = 3001,
                message = "Countries retrieved successfully.",
                data = countryService.findAll()
            )
        }
        // Endpoint to retrieve a country by its code
        get("/{countryCode}") {
            with(call) {
                doIfParamExists("countryCode") { countryCode ->
                    generateSuccessResponse(
                        code = 3002,
                        message = "Country found.",
                        data = countryService.findByCode(countryCode)
                    )
                }
            }
        }
    }
}
