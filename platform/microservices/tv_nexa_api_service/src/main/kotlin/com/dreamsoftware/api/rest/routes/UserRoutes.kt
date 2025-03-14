package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.controllers.IUserController
import com.dreamsoftware.api.rest.utils.doIfParamExists
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
 * @property userController An instance of the [IUserController] interface for handling user-related operations.
 */
fun Route.userRoutes() {

    val userController by inject<IUserController>()

    /**
     * Defines the routes under the "/user" endpoint for user user-related operations.
     */
    route("/user") {

        /**
         * Endpoint for retrieving the user profile.
         * Accepts GET requests to "/user/" and retrieves the user profile using the [userController.getUserProfile] method.
         * Generates a success response with a code of 8001, a message indicating successful retrieval of the user profile,
         * and data containing the user profile information.
         */
        get("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8001,
                    message = "User profile retrieved successfully.",
                    data = userController.getUserDetail(fetchAuthUserUuidOrThrow())
                )
            }
        }

        /**
         * Endpoint for updating the user profile.
         * Accepts PUT requests to "/user/" and updates the user profile using the [userController.updateUserProfile] method.
         * Generates a success response with a code of 8002, a message indicating successful update of the user profile,
         * and data containing any additional information related to the update.
         */
        put("/") {
            with(call) {
                generateSuccessResponse(
                    code = 8002,
                    message = "User detail updated successfully.",
                    data = userController.updateUserDetail(fetchAuthUserUuidOrThrow(), receive())
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
                        data = userController.getUserProfiles(fetchAuthUserUuidOrThrow())
                    )
                }
            }

            /**
             * Defines a route handler for retrieving a user profile by its profile ID.
             * Retrieves the user profile details for the specified profile ID and returns a success response.
             *
             * @param profileId The ID of the profile to retrieve.
             * @return A success response containing the retrieved profile details.
             */
            get("/{profileId}") {
                with(call) {
                    generateSuccessResponse(
                        code = 8004,
                        message = "Profile retrieved successfully",
                        data = userController.getUserProfileDetail(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId")
                        )
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
                        code = 8005,
                        message = "User profile updated successfully.",
                        data = userController.updateUserProfile(
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
                        code = 8006,
                        message = "User profile deleted successfully.",
                        data = userController.deleteUserProfile(
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
                        code = 8007,
                        message = "User profile created successfully.",
                        data = userController.createProfile(
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
                    val isVerificationSuccess = userController.verifyPin(
                        userUuid = fetchAuthUserUuidOrThrow(),
                        profileUUID = getUUIDParamOrThrow("profileId"),
                        data = receive()
                    )
                    generateSuccessResponse(
                        code = if(isVerificationSuccess) 8008 else 8009,
                        message =  "PIN verification completed",
                        data = if(isVerificationSuccess) {
                            "PIN verification successful."
                        } else {
                            "PIN verification failed. Please check your PIN and try again."
                        }
                    )
                }
            }

            /**
             * Endpoint to retrieve blocked channels for a user's profile identified by {profileId}.
             *
             * This endpoint allows an authenticated user to retrieve the list of blocked channels for their profile.
             */
            get("/{profileId}/blocked-channels") {
                with(call) {
                    generateSuccessResponse(
                        code = 8010,
                        message = "Blocked channels retrieved successfully.",
                        data = userController.getBlockedChannels(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId")
                        )
                    )
                }
            }

            /**
             * Route handler for saving a channel as blocked for a user's profile.
             * This endpoint allows users to add a channel to their list of blocked channels.
             */
            put("/{profileId}/blocked-channels/{channelId}") {
                with(call) {
                    doIfParamExists("channelId") { channelId ->
                        userController.saveBlockedChannel(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            channelId = channelId
                        )
                        generateSuccessResponse(
                            code = 8011,
                            message = "Save blocked channel completed.",
                            data = "Save channel $channelId as blocked successfully"
                        )
                    }
                }
            }

            /**
             * Route handler for deleting a blocked channel from a user's profile.
             * This endpoint allows users to remove a channel from their list of blocked channels.
             */
            delete("/{profileId}/blocked-channels/{channelId}") {
                with(call) {
                    doIfParamExists("channelId") { channelId ->
                        userController.deleteBlockedChannel(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            channelId = channelId
                        )
                        generateSuccessResponse(
                            code = 8012,
                            message = "Delete blocked channel completed.",
                            data = "delete channel $channelId from blocked list successfully"
                        )
                    }
                }
            }

            /**
             * Endpoint to retrieve favorite channels for a user's profile identified by {profileId}.
             *
             * This endpoint allows an authenticated user to retrieve the list of favorite channels for their profile.
             */
            get("/{profileId}/favorite-channels") {
                with(call) {
                    generateSuccessResponse(
                        code = 8013,
                        message = "Favorite channels retrieved successfully.",
                        data = userController.getFavoriteChannels(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId")
                        )
                    )
                }
            }

            /**
             * Route handler for saving a channel as a favorite for a specific user profile.
             * This endpoint allows users to save a channel as a favorite for a given user profile.
             */
            put("/{profileId}/favorite-channels/{channelId}") {
                with(call) {
                    doIfParamExists("channelId") { channelId ->
                        userController.saveFavoriteChannel(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            channelId = channelId
                        )
                        generateSuccessResponse(
                            code = 8014,
                            message = "Save favorite channel completed.",
                            data = "Save channel $channelId as favorite successfully"
                        )
                    }
                }
            }

            /**
             * Route handler for deleting a favorite channel from a user's profile.
             * This endpoint allows users to remove a channel from their list of favorite channels.
             */
            delete("/{profileId}/favorite-channels/{channelId}") {
                with(call) {
                    doIfParamExists("channelId") { channelId ->
                        userController.deleteFavoriteChannel(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            channelId = channelId
                        )
                        generateSuccessResponse(
                            code = 8015,
                            message = "Delete favorite channel completed.",
                            data = "delete channel $channelId from favorites successfully"
                        )
                    }
                }
            }

            /**
             * Endpoint to check if a channel is saved as a favorite for a user profile.
             * It returns a success response indicating whether the channel is saved as a favorite or not.
             **/
            get("/{profileId}/favorite-channels/{channelId}") {
                with(call) {
                    doIfParamExists("channelId") { channelId ->
                        val isSavedAsFavorite = userController.isChannelSavedAsFavorite(
                            userUuid = fetchAuthUserUuidOrThrow(),
                            profileUUID = getUUIDParamOrThrow("profileId"),
                            channelId = channelId
                        )
                        generateSuccessResponse(
                            code = if(isSavedAsFavorite) 8016 else 8017,
                            message = "Check favorite channel completed",
                            data = if (isSavedAsFavorite) "Channel is saved as favorite." else "Channel is not saved as favorite.",
                        )
                    }
                }
            }
        }
    }
}