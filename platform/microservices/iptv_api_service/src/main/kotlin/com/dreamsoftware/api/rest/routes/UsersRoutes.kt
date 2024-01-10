package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.usersRoutes() {

    val userService by inject<IUserService>()

    route("/user") {

        post("/signup") {
            with(call) {
                userService.signUp(receive())
                generateSuccessResponse(
                    code = 7002,
                    message = "User registered successfully.",
                    data = "User registered"
                )
            }
        }

        post("/signin") {
            with(call) {
                generateSuccessResponse(
                    code = 7002,
                    message = "User authenticated successfully.",
                    data = userService.signIn(receive())
                )
            }
        }

        // Get user profile endpoint
        get("/profile") {
            // Assume authentication mechanism is in place to get user details
           /* val userId = call.authentication.principal<UserIdPrincipal>()?.name
            userId?.let {
                val userProfile = userService.getUserProfile(userId)
                call.respond(userProfile)
            } ?: call.respond(HttpStatusCode.Unauthorized, "User not authenticated")*/
        }

        // Update user profile endpoint
        put("/profile") {

        }
    }
}