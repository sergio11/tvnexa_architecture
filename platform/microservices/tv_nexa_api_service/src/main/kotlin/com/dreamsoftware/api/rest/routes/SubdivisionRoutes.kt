package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.controllers.ISubdivisionController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to subdivisions in the application.
 * These routes include functionalities such as retrieving all subdivisions and finding a subdivision by its code.
 *
 * @property subdivisionService An instance of the [ISubdivisionController] interface for handling subdivision-related operations.
 */
fun Route.subdivisionRoutes() {
    val subdivisionService by inject<ISubdivisionController>()

    /**
     * Defines the routes under the "/subdivisions" endpoint for subdivision-related operations.
     */
    route("/subdivisions") {

        /**
         * Endpoint for retrieving all subdivisions.
         * Accepts GET requests to "/subdivisions/" and retrieves all subdivisions using the [subdivisionService.findAll] method.
         * Generates a success response with a code of 6001, a message indicating successful retrieval of subdivisions,
         * and data containing the list of subdivisions.
         */
        get("/") {
            call.generateSuccessResponse(
                code = 6001,
                message = "Subdivisions retrieved successfully.",
                data = subdivisionService.findAll()
            )
        }

        /**
         * Endpoint for finding a subdivision by its code.
         * Accepts GET requests to "/subdivisions/{subdivisionCode}" and retrieves a subdivision by its code
         * using the [subdivisionService.findByCode] method.
         * Generates a success response with a code of 6002, a message indicating successful subdivision retrieval,
         * and data containing the details of the found subdivision.
         *
         * Path Parameter:
         * - subdivisionCode: The code of the subdivision to be retrieved.
         */
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
