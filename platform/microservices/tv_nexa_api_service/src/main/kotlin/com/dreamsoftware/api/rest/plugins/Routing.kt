package com.dreamsoftware.api.rest.plugins

import com.dreamsoftware.api.rest.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting() {
    routing {
        trace { application.log.trace(it.buildText()) }
        route("/api/v1") {
            authenticate(JWT_AUTHENTICATION) {
                categoriesRoutes()
                countryRoutes()
                channelRoutes()
                regionsRoutes()
                subdivisionRoutes()
                epgChannelProgrammeRoutes()
                userRoutes()
            }
            accountsRoutes()
        }
    }
}
