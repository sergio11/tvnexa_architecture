package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.services.IChannelService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.channelRoutes() {
    val channelService by inject<IChannelService>()

    route("/channels") {
        // Endpoint to retrieve all channels
        get("/") {
            val channels = channelService.findAll()
            call.respond(channels)
        }

        // Endpoint to retrieve a channel by its ID
        get("/{channelId}") {
            with(call) {
                parameters["channelId"]?.let { channelId ->
                    respond(channelService.findById(channelId))
                } ?: run {
                    respond(HttpStatusCode.BadRequest, "Invalid channel ID")
                }
            }
        }

        // Endpoint to filter channels by category and/or country
        get("/filter") {
            with(call) {
                val category = parameters["category"]
                val country = parameters["country"]
                val filteredChannels = channelService.filterByCategoryAndCountry(category, country)
                respond(filteredChannels)
            }
        }
    }
}
