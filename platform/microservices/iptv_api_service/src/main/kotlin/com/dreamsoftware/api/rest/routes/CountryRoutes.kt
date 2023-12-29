package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.services.ICountryService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Route.countryRoutes() {
    val countryService by inject<ICountryService>()

    route("/countries") {
        // Endpoint to retrieve all countries
        get("/") {
            val countries = countryService.findAll()
            call.respond(countries)
        }
        // Endpoint to retrieve a country by its code
        get("/{countryCode}") {
            val countryCode = call.parameters["countryCode"]
            if (countryCode != null) {
                val country = countryService.findByCode(countryCode)
                call.respond(country)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid country code")
            }
        }
    }
}
