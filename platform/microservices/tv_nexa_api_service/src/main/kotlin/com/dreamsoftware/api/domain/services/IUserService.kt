package com.dreamsoftware.api.domain.services

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
interface IUserService {

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
     * Suspended function to update a user's profile based on the provided data.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the profile to be updated.
     * @param data The [UpdatedProfileRequestDTO] containing the updates to be applied to the profile.
     * @return A [ProfileResponseDTO] representing the updated user profile.
     * @throws [AppException.InternalServerError] if an unexpected internal error occurs.
     * @throws [AppException.NotFoundException.UserNotFoundException] if the specified user is not found.
     * @throws [AppException.UserProfileAlreadyExistsException] if there is an attempt to create a duplicate profile.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.UserProfileAlreadyExistsException::class
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
     * Deletes a user's profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the profile to be deleted.
     * @throws [AppException.InternalServerError] if an unexpected internal error occurs.
     * @throws [AppException.NotFoundException.UserNotFoundException] if the specified user is not found.
     * @throws [AppException.NotFoundException.ProfileNotFoundException] if the specified profile is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.ProfileNotFoundException::class
    )
    suspend fun deleteUserProfile(userUuid: UUID, profileUUID: UUID)

    /**
     * Creates a new profile for the authenticated user.
     *
     * @param userUuid The unique identifier of the user.
     * @param data The [CreateProfileRequestDTO] containing the data for the new profile.
     * @return A [ProfileResponseDTO] representing the newly created user profile.
     * @throws [AppException.InternalServerError] if an unexpected internal error occurs.
     * @throws [AppException.UserProfileAlreadyExistsException] if there is an attempt to create a duplicate profile.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.UserProfileAlreadyExistsException::class
    )
    suspend fun createProfile(userUuid: UUID, data: CreateProfileRequestDTO)

    /**
     * Verifies the PIN of a user's profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the profile to be verified.
     * @param data The PinVerificationRequest to be verified.
     * @return `true` if the PIN is verified successfully, `false` otherwise.
     * @throws [AppException.NotFoundException.ProfileNotFoundException] if the specified profile is not found.
     */
    @Throws(AppException.NotFoundException.ProfileNotFoundException::class)
    suspend fun verifyPin(userUuid: UUID, profileUUID: UUID, data: PinVerificationRequestDTO): Boolean

    /**
     * Retrieves a list of blocked channels for the specified user and user profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the user profile.
     * @return A list of [SimpleChannelResponseDTO] objects representing the blocked channels.
     * @throws AppException.InternalServerError if there is an internal server error during the operation.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun getBlockedChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO>

    /**
     * Retrieves a list of favorite channels for the specified user and user profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the user profile.
     * @return A list of [SimpleChannelResponseDTO] objects representing the favorite channels.
     * @throws AppException.InternalServerError if there is an internal server error during the operation.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun getFavoriteChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO>
}