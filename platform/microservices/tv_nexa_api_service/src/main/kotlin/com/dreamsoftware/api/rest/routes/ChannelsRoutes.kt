package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.rest.utils.Constants.DEFAULT_OFFSET
import com.dreamsoftware.api.rest.utils.Constants.DEFAULT_PAGE_SIZE
import com.dreamsoftware.api.rest.utils.generateErrorResponse
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.utils.getLongParamOrDefault
import com.dreamsoftware.api.domain.services.IChannelService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to channels in the application.
 * These routes include functionalities such as retrieving channels based on category, country, and pagination,
 * as well as finding a channel by its ID.
 *
 * @property channelService An instance of the [IChannelService] interface for handling channel-related operations.
 */
fun Route.channelRoutes() {
    val channelService by inject<IChannelService>()

    /**
     * Defines the routes under the "/channels" endpoint for channel-related operations.
     */
    route("/channels") {

        /**
         * Endpoint for retrieving channels based on category, country, and pagination.
         * Accepts GET requests to "/channels/" and retrieves channels using parameters such as category, country,
         * offset, and limit. Utilizes the [channelService.findByCategoryAndCountryPaginated] method.
         * Generates a success response with a code of 2001, a message indicating successful channel retrieval,
         * and data containing the paginated list of channels.
         *
         * Query Parameters:
         * - category: The category of the channels.
         * - country: The country of the channels.
         * - offset: The offset for pagination (default is set to [DEFAULT_OFFSET]).
         * - limit: The limit for the number of channels per page (default is set to [DEFAULT_PAGE_SIZE]).
         */
        get("/") {
            with(call) {
                val category = parameters["category"]
                val country = parameters["country"]
                val offset = getLongParamOrDefault(paramName = "offset", defaultValue = DEFAULT_OFFSET)
                val limit = getLongParamOrDefault(paramName = "limit", defaultValue = DEFAULT_PAGE_SIZE)
                generateSuccessResponse(
                    code = 2001,
                    message = "Channels retrieved successfully.",
                    data = channelService.findByCategoryAndCountryPaginated(category, country, offset, limit)
                )
            }
        }

        /**
         * Endpoint for finding a channel by its ID.
         * Accepts GET requests to "/channels/{channelId}" and retrieves a channel by its ID
         * using the [channelService.findById] method.
         * Generates a success response with a code of 2002, a message indicating successful channel retrieval,
         * and data containing the details of the found channel.
         *
         * Path Parameter:
         * - channelId: The ID of the channel to be retrieved.
         */
        get("/{channelId}") {
            with(call) {
                parameters["channelId"]?.let { channelId ->
                    generateSuccessResponse(
                        code = 2002,
                        message = "Channel found.",
                        data = channelService.findById(channelId)
                    )
                } ?: run {
                    generateErrorResponse(ErrorType.BAD_REQUEST)
                }
            }
        }
    }
}