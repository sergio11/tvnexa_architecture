package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.utils.fetchAuthUserUuidOrThrow
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to user profiles in the application.
 * These routes include functionalities such as retrieving and updating user profiles.
 *
 * @property userService An instance of the [IUserService] interface for handling user-related operations.
 */
fun Route.profileRoutes() {

    val userService by inject<IUserService>()

    /**
     * Defines the routes under the "/profile" endpoint for user profile-related operations.
     */
    route("/profile") {

        /**
         * Endpoint for retrieving the user profile.
         * Accepts GET requests to "/profile/" and retrieves the user profile using the [userService.getUserProfile] method.
         * Generates a success response with a code of 8001, a message indicating successful retrieval of the user profile,
         * and data containing the user profile information.
         */
        get("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8001,
                    message = "User profile retrieved successfully.",
                    data = userService.getUserProfile(fetchAuthUserUuidOrThrow())
                )
            }
        }

        /**
         * Endpoint for updating the user profile.
         * Accepts PUT requests to "/profile/" and updates the user profile using the [userService.updateUserProfile] method.
         * Generates a success response with a code of 8002, a message indicating successful update of the user profile,
         * and data containing any additional information related to the update.
         */
        put("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8002,
                    message = "User profile updated successfully.",
                    data = userService.updateUserProfile(fetchAuthUserUuidOrThrow(), receive())
                )
            }
        }
    }
}