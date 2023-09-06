package com.dreamsoftware.api.routes

import com.dreamsoftware.api.services.CountryServiceException
import com.dreamsoftware.api.services.ICountryService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Routing.countryRoutes() {
    val countryService by inject<ICountryService>()

    route("/countries") {
        // Endpoint to retrieve all countries
        get("/") {
            handleExceptions {
                val countries = countryService.findAll()
                call.respond(countries)
            }
        }

        // Endpoint to retrieve a country by its code
        get("/{countryCode}") {
            val countryCode = call.parameters["countryCode"]
            if (countryCode != null) {
                handleExceptions {
                    val country = countryService.findByCode(countryCode)
                    call.respond(country)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid country code")
            }
        }
    }
}

// Custom exception handling function
suspend fun PipelineContext<*, ApplicationCall>.handleExceptions(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: CountryServiceException.InternalServerError) {
        // Internal server error exception
        call.respond(HttpStatusCode.InternalServerError, "Internal server error: ${e.message}")
    } catch (e: CountryServiceException.CountryNotFoundException) {
        // Country not found exception
        call.respond(HttpStatusCode.NotFound, "Country not found: ${e.message}")
    }
}
