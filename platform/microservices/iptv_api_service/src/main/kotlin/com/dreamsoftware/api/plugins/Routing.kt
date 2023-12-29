package com.dreamsoftware.api.plugins

import com.dreamsoftware.api.rest.routes.channelRoutes
import com.dreamsoftware.api.rest.routes.categoriesRoutes
import com.dreamsoftware.api.rest.routes.countryRoutes
import com.dreamsoftware.api.rest.routes.epgChannelProgrammeRoutes
import com.dreamsoftware.api.rest.routes.regionsRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        trace { application.log.trace(it.buildText()) }
        route("/api/v1") {
            categoriesRoutes()
            countryRoutes()
            channelRoutes()
            regionsRoutes()
            epgChannelProgrammeRoutes()
        }
    }
}
