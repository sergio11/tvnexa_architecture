package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.utils.fetchAuthUserUuidOrThrow
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.profileRoutes() {

    val userService by inject<IUserService>()

    route("/profile") {

        // Get user profile endpoint
        get("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8001,
                    message = "User profile retrieved successfully.",
                    data = userService.getUserProfile(fetchAuthUserUuidOrThrow())
                )
            }
        }

        // Update user profile endpoint
        put("/") {

        }
    }
}