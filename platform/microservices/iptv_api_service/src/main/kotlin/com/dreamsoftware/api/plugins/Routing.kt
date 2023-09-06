package com.dreamsoftware.api.plugins

import com.dreamsoftware.api.routes.categoriesRoutes
import com.dreamsoftware.api.routes.countryRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        categoriesRoutes()
        countryRoutes()
    }
}
