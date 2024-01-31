package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.utils.fetchAuthUserUuidOrThrow
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.utils.getUUIDParamOrThrow
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
fun Route.userRoutes() {

    val userService by inject<IUserService>()

    /**
     * Defines the routes under the "/user" endpoint for user user-related operations.
     */
    route("/user") {

        /**
         * Endpoint for retrieving the user profile.
         * Accepts GET requests to "/user/" and retrieves the user profile using the [userService.getUserProfile] method.
         * Generates a success response with a code of 8001, a message indicating successful retrieval of the user profile,
         * and data containing the user profile information.
         */
        get("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8001,
                    message = "User profile retrieved successfully.",
                    data = userService.getUserDetail(fetchAuthUserUuidOrThrow())
                )
            }
        }

        /**
         * Endpoint for updating the user profile.
         * Accepts PUT requests to "/user/" and updates the user profile using the [userService.updateUserProfile] method.
         * Generates a success response with a code of 8002, a message indicating successful update of the user profile,
         * and data containing any additional information related to the update.
         */
        put("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8002,
                    message = "User detail updated successfully.",
                    data = userService.updateUserDetail(fetchAuthUserUuidOrThrow(), receive())
                )
            }
        }

        route("/profiles") {

            /**
             * Endpoint to retrieve profiles for the authenticated user
             */
            get("/") {
                with(call) {
                    generateSuccessResponse(
                        code = 8003,
                        message = "Profiles retrieved successfully",
                        data = userService.getUserProfiles(fetchAuthUserUuidOrThrow())
                    )
                }
            }

            /**
             * Handles the HTTP PUT request to update a user's profile identified by {profileId}.
             *
             * This endpoint allows an authenticated user to update their profile information.
             */
            put("/{profileId}") {
                with(call) {
                    generateSuccessResponse(
                        code = 8004,
                        message = "User profile updated successfully.",
                        data = userService.updateUserProfile(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            data = receive()
                        )
                    )
                }
            }

            /**
             * Handles the HTTP DELETE request to delete a user's profile identified by {profileId}.
             *
             * This endpoint allows an authenticated user to delete their profile.
             */
            delete("/{profileId}") {
                with(call) {
                    generateSuccessResponse(
                        code = 8005,
                        message = "User profile deleted successfully.",
                        data = userService.deleteUserProfile(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId")
                        )
                    )
                }
            }

            /**
             * Handles the HTTP POST request to create a new profile for the authenticated user.
             *
             * This endpoint allows an authenticated user to create a new profile.
             */
            post("/") {
                with(call) {
                    generateSuccessResponse(
                        code = 8006,
                        message = "User profile created successfully.",
                        data = userService.createProfile(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            data = receive()
                        )
                    )
                }
            }

            /**
             * Handles the HTTP POST request to verify the PIN of a user's profile.
             *
             * This endpoint allows an authenticated user to verify the PIN of their profile.
             */
            post("/{profileId}/verify-pin") {
                with(call) {
                    generateSuccessResponse(
                        code = 8007,
                        message = "PIN verification successful.",
                        data = userService.verifyPin(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            data = receive()
                        )
                    )
                }
            }
        }
    }
}