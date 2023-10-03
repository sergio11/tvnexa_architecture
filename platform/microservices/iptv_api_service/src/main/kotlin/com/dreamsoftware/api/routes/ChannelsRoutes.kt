package com.dreamsoftware.api.routes

import com.dreamsoftware.api.services.ChannelServiceException
import com.dreamsoftware.api.services.IChannelService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Routing.channelRoutes() {
    val channelService by inject<IChannelService>()

    route("/channels") {
        // Endpoint to retrieve all channels
        get("/") {
            handleChannelExceptions {
                val channels = channelService.findAll()
                call.respond(channels)
            }
        }

        // Endpoint to retrieve a channel by its ID
        get("/{channelId}") {
            val channelId = call.parameters["channelId"]
            if (channelId != null) {
                handleChannelExceptions {
                    val channel = channelService.findById(channelId)
                    call.respond(channel)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid channel ID")
            }
        }

        // Endpoint to filter channels by category and/or country
        get("/filter") {
            val category = call.parameters["category"]
            val country = call.parameters["country"]

            handleChannelExceptions {
                val filteredChannels = channelService.filterByCategoryAndCountry(category, country)
                call.respond(filteredChannels)
            }
        }
    }
}

// Custom exception handling function for channels
suspend fun PipelineContext<*, ApplicationCall>.handleChannelExceptions(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: ChannelServiceException.InternalServerError) {
        // Internal server error exception for channels
        call.respond(HttpStatusCode.InternalServerError, "Internal server error: ${e.message}")
    } catch (e: ChannelServiceException.ChannelNotFoundException) {
        // Channel not found exception
        call.respond(HttpStatusCode.NotFound, "Channel not found: ${e.message}")
    }
}
