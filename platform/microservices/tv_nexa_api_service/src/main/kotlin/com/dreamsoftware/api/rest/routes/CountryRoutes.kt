package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.controllers.ICountryController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to countries in the application.
 * These routes include functionalities such as retrieving all countries and finding a country by its code.
 *
 * @property countryController An instance of the [ICountryController] interface for handling country-related operations.
 */
fun Route.countryRoutes() {
    val countryController by inject<ICountryController>()

    /**
     * Defines the routes under the "/countries" endpoint for country-related operations.
     */
    route("/countries") {

        /**
         * Endpoint for retrieving all countries.
         * Accepts GET requests to "/countries/" and retrieves all countries using the [countryController.findAll] method.
         * Generates a success response with a code of 3001, a message indicating successful retrieval of countries,
         * and data containing the list of countries.
         */
        get("/") {
            call.generateSuccessResponse(
                code = 3001,
                message = "Countries retrieved successfully.",
                data = countryController.findAll()
            )
        }

        /**
         * Endpoint for finding a country by its code.
         * Accepts GET requests to "/countries/{countryCode}" and retrieves a country by its code
         * using the [countryService.findByCode] method.
         * Generates a success response with a code of 3002, a message indicating successful country retrieval,
         * and data containing the details of the found country.
         *
         * Path Parameter:
         * - countryCode: The code of the country to be retrieved.
         */
        get("/{countryCode}") {
            with(call) {
                doIfParamExists("countryCode") { countryCode ->
                    generateSuccessResponse(
                        code = 3002,
                        message = "Country found.",
                        data = countryController.findByCode(countryCode)
                    )
                }
            }
        }

        /**
         * Defines a route handler for searching countries by name.
         * Retrieves countries whose names match the provided search term and returns a success response.
         *
         * @param term The search term used to find countries by name.
         * @return A success response containing the countries found by name.
         */
        get("/search") {
            with(call) {
                doIfParamExists("term") {
                    generateSuccessResponse(
                        code = 3003,
                        message = "Countries found by name.",
                        data = countryController.findByNameLike(it)
                    )
                }
            }
        }
    }
}
