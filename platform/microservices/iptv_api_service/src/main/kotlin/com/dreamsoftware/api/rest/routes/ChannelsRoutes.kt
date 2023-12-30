package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.model.ErrorType
import com.dreamsoftware.api.rest.utils.Constants.DEFAULT_OFFSET
import com.dreamsoftware.api.rest.utils.Constants.DEFAULT_PAGE_SIZE
import com.dreamsoftware.api.rest.utils.generateErrorResponse
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.utils.getLongParamOrDefault
import com.dreamsoftware.api.services.IChannelService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.channelRoutes() {
    val channelService by inject<IChannelService>()

    route("/channels") {
        // Endpoint to retrieve all channels
        get("/") {
            with(call) {
                val offset = getLongParamOrDefault(paramName = "offset", defaultValue = DEFAULT_OFFSET)
                val limit = getLongParamOrDefault(paramName = "limit", defaultValue = DEFAULT_PAGE_SIZE)
                generateSuccessResponse(
                    code = 2001,
                    message = "Channels retrieved successfully.",
                    data = channelService.findPaginated(offset, limit)
                )
            }

        }

        // Endpoint to retrieve a channel by its ID
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

        // Endpoint to filter channels by category and/or country
        get("/filter") {
            with(call) {
                val category = parameters["category"]
                val country = parameters["country"]
                generateSuccessResponse(
                    code = 2003,
                    message = "Channels filtered successfully by category '${category ?: "all"}' and country '${country ?: "all"}'.",
                    data =  channelService.filterByCategoryAndCountry(category, country)
                )
            }
        }
    }
}
