package com.dreamsoftware.api.rest.controllers

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.request.*
import com.dreamsoftware.api.rest.dto.response.AuthResponseDTO
import com.dreamsoftware.api.rest.dto.response.ProfileResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO
import com.dreamsoftware.api.rest.dto.response.UserResponseDTO
import java.util.*

/**
 * Interface defining user-related operations for sign-up, sign-in, fetching user profiles,
 * and updating user profiles.
 */
interface IUserController {

    /**
     * Registers a new user.
     *
     * @param signUpRequest Data representing sign-up request details.
     * @return An [AuthResponseDTO] containing user token and profile details upon successful sign-up.
     * @throws AppException.InternalServerError if an internal server error occurs during user registration.
     * @throws AppException.UserAlreadyExistsException if the user already exists.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.UserAlreadyExistsException::class
    )
    suspend fun signUp(signUpRequest: SignUpRequestDTO)

    /**
     * Authenticates an existing user.
     *
     * @param signInRequest Data representing sign-in request details.
     * @return An [AuthResponseDTO] containing user token and profile details upon successful sign-in.
     * @throws AppException.InternalServerError if an internal server error occurs during user sign-in.
     * @throws AppException.InvalidCredentialsException if the provided credentials are invalid.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.InvalidCredentialsException::class
    )
    suspend fun signIn(signInRequest: SignInRequestDTO): AuthResponseDTO

    /**
     * Retrieves the user profile based on the provided user ID.
     *
     * @param uuid The unique identifier of the user.
     * @return A [UserResponseDTO] containing user profile details.
     * @throws AppException.InternalServerError if an internal server error occurs while fetching the user profile.
     * @throws AppException.NotFoundException.UserNotFoundException if the user with the given UUID is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class
    )
    suspend fun getUserDetail(uuid: UUID): UserResponseDTO

    /**
     * Retrieves the profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileResponseDTO] objects representing the user's profiles.
     *
     * @throws AppException.InternalServerError If there is an internal server error.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class
    )
    suspend fun getUserProfiles(uuid: UUID): List<ProfileResponseDTO>

    /**
     * Retrieves detailed information about a user profile.
     * @param userUuid The UUID of the user associated with the profile.
     * @param profileUUID The UUID of the profile for which detailed information will be retrieved.
     * @return A ProfileResponseDTO object containing detailed information about the user profile.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ProfileNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class
    )
    suspend fun getUserProfileDetail(userUuid: UUID, profileUUID: UUID): ProfileResponseDTO

    /**
     * Updates a user profile with the provided data.
     * @param userUuid The UUID of the user associated with the profile.
     * @param profileUUID The UUID of the profile to be updated.
     * @param data The UpdatedProfileRequestDTO containing the updated profile data.
     * @return A ProfileResponseDTO object containing the updated information of the user profile.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ProfileNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class
    )
    suspend fun updateUserProfile(userUuid: UUID, profileUUID: UUID, data: UpdatedProfileRequestDTO): ProfileResponseDTO

    /**
     * Updates the user profile based on the provided user ID and updated information.
     *
     * @param uuid The unique identifier of the user.
     * @param updatedUser Data representing updated user profile details.
     * @return A [UserResponseDTO] containing updated user profile details.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.UserAlreadyExistsException::class
    )
    suspend fun updateUserDetail(uuid: UUID, updatedUser: UpdatedUserRequestDTO): UserResponseDTO

    /**
     * Deletes a user profile.
     * @param userUuid The UUID of the user requesting the profile deletion.
     * @param profileUUID The UUID of the profile to be deleted.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.ProfileNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class
    )
    suspend fun deleteUserProfile(userUuid: UUID, profileUUID: UUID)

    /**
     * Creates a new user profile.
     * @param userUuid The UUID of the user for whom the profile will be created.
     * @param data The CreateProfileRequestDTO containing the data for creating the profile.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.UserProfilesLimitReachedException If the user has reached the limit of allowed profiles.
     * @throws AppException.UserProfileAlreadyExistsException If a profile already exists for the user with the specified data.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.UserProfilesLimitReachedException::class,
        AppException.UserProfileAlreadyExistsException::class
    )
    suspend fun createProfile(userUuid: UUID, data: CreateProfileRequestDTO)

    /**
     * Verifies a PIN for a given user profile.
     * @param userUuid The UUID of the user associated with the profile.
     * @param profileUUID The UUID of the profile for which the PIN will be verified.
     * @param data The PinVerificationRequestDTO containing the PIN data to be verified.
     * @return true if the PIN is verified successfully, false otherwise.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class
    )
    suspend fun verifyPin(userUuid: UUID, profileUUID: UUID, data: PinVerificationRequestDTO): Boolean

    /**
     * Retrieves the list of blocked channels for a given user profile.
     * @param userUuid The UUID of the user requesting the blocked channels.
     * @param profileUUID The UUID of the profile for which blocked channels are being fetched.
     * @return A list of SimpleChannelResponseDTO representing the blocked channels.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun getBlockedChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO>

    /**
     * Saves a channel as blocked for a given user profile.
     * @param userUuid The UUID of the user associated with the operation.
     * @param profileUUID The UUID of the profile for which the channel will be saved as blocked.
     * @param channelId The ID of the channel to be saved as blocked.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun saveBlockedChannel(userUuid: UUID, profileUUID: UUID, channelId: String)

    /**
     * Deletes a blocked channel from a user profile.
     * @param userUuid The UUID of the user associated with the operation.
     * @param profileUUID The UUID of the profile for which the blocked channel will be deleted.
     * @param channelId The ID of the channel to be deleted from the blocked list.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun deleteBlockedChannel(userUuid: UUID, profileUUID: UUID, channelId: String)

    /**
     * Retrieves the list of favorite channels for a given user profile.
     * @param userUuid The UUID of the user requesting the favorite channels.
     * @param profileUUID The UUID of the profile for which favorite channels are being fetched.
     * @return A list of SimpleChannelResponseDTO representing the favorite channels.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun getFavoriteChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO>

    /**
     * Saves a channel as a favorite for a given user profile.
     * @param userUuid The UUID of the user associated with the operation.
     * @param profileUUID The UUID of the profile for which the channel will be saved as a favorite.
     * @param channelId The ID of the channel to be saved as a favorite.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun saveFavoriteChannel(userUuid: UUID, profileUUID: UUID, channelId: String)

    /**
     * Checks if a channel is saved as a favorite by a user.
     *
     * @param userUuid The UUID of the user whose favorites are being checked.
     * @param profileUUID The UUID of the user's profile.
     * @param channelId The ID of the channel being checked.
     * @return `true` if the channel is saved as a favorite by the user, `false` otherwise.
     * @throws AppException.InternalServerError If there is an internal server error in the application.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the user is not allowed to access the channel.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun isChannelSavedAsFavorite(userUuid: UUID, profileUUID: UUID, channelId: String): Boolean

    /**
     * Deletes a favorite channel for a given user profile.
     * @param userUuid The UUID of the user associated with the operation.
     * @param profileUUID The UUID of the profile for which the favorite channel will be deleted.
     * @param channelId The ID of the channel to be deleted as a favorite.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ChannelNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    suspend fun deleteFavoriteChannel(userUuid: UUID, profileUUID: UUID, channelId: String)
}