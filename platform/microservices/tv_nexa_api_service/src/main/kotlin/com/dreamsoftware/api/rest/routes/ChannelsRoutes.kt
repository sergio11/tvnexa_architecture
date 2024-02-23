package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.model.ErrorType
import com.dreamsoftware.api.rest.utils.Constants.DEFAULT_OFFSET
import com.dreamsoftware.api.rest.utils.Constants.DEFAULT_PAGE_SIZE
import com.dreamsoftware.api.rest.controllers.IChannelController
import com.dreamsoftware.api.rest.utils.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to channels in the application.
 * These routes include functionalities such as retrieving channels based on category, country, and pagination,
 * as well as finding a channel by its ID.
 *
 * @property channelService An instance of the [IChannelController] interface for handling channel-related operations.
 */
fun Route.channelRoutes() {
    val channelService by inject<IChannelController>()

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
                val category = getStringParam("category")
                val country = getStringParam("country")
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
                getStringParam("channelId")?.let { channelId ->
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

        /**
         * New endpoint for searching channels by name.
         * Accepts GET requests to "/channels/search" and retrieves channels by name.
         * Generates a success response with a code of 2003, a message indicating successful channel search by name,
         * and data containing the list of channels matching the search term.
         *
         * Query Parameters:
         * - name: The search term for channel names.
         */
        get("/search") {
            with(call) {
                doIfParamExists("term") {
                    generateSuccessResponse(
                        code = 2003,
                        message = "Channels found by name.",
                        data = channelService.findByNameLike(it)
                    )
                }
            }
        }
    }
}
