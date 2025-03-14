package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.controllers.IRegionController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to regions in the application.
 * These routes include functionalities such as retrieving all regions and finding a region by its code.
 *
 * @property regionController An instance of the [IRegionController] interface for handling region-related operations.
 */
fun Route.regionsRoutes() {
    val regionController by inject<IRegionController>()

    /**
     * Defines the routes under the "/regions" endpoint for region-related operations.
     */
    route("/regions") {

        /**
         * Endpoint for retrieving all regions.
         * Accepts GET requests to "/regions/" and retrieves all regions using the [regionController.findAll] method.
         * Generates a success response with a code of 5001, a message indicating successful retrieval of regions,
         * and data containing the list of regions.
         */
        get("/") {
            call.generateSuccessResponse(
                code = 5001,
                message = "Regions retrieved successfully.",
                data = regionController.findAll()
            )
        }

        /**
         * Endpoint for finding a region by its code.
         * Accepts GET requests to "/regions/{regionCode}" and retrieves a region by its code
         * using the [regionController.findByCode] method.
         * Generates a success response with a code of 5002, a message indicating successful region retrieval,
         * and data containing the details of the found region.
         *
         * Path Parameter:
         * - regionCode: The code of the region to be retrieved.
         */
        get("/{regionCode}") {
            with(call) {
                doIfParamExists("regionCode") { regionCode ->
                    generateSuccessResponse(
                        code = 5002,
                        message = "Region found.",
                        data = regionController.findByCode(regionCode)
                    )
                }
            }
        }
    }
}
