package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to user accounts in the application.
 * These routes include functionalities such as user registration and authentication.
 *
 * @property userService An instance of the [IUserService] interface for handling user-related operations.
 */
fun Route.accountsRoutes() {

    val userService by inject<IUserService>()

    /**
     * Defines the routes under the "/accounts" endpoint for user-related operations.
     */
    route("/accounts") {

        /**
         * Endpoint for user registration.
         * Accepts POST requests to "/accounts/signup" and registers a new user using the [userService.signUp] method.
         * Generates a success response with a code of 7002, a message indicating successful registration,
         * and data containing a confirmation message.
         */
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

        /**
         * Endpoint for user authentication.
         * Accepts POST requests to "/accounts/signin" and authenticates a user using the [userService.signIn] method.
         * Generates a success response with a code of 7002, a message indicating successful authentication,
         * and data containing the user information retrieved from the [userService.signIn] method.
         */
        post("/signin") {
            with(call) {
                generateSuccessResponse(
                    code = 7002,
                    message = "User authenticated successfully.",
                    data = userService.signIn(receive())
                )
            }
        }
    }
}