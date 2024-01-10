package com.dreamsoftware.api.domain.services

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.request.SignInRequestDTO
import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.api.rest.dto.request.UpdatedUserRequestDTO
import com.dreamsoftware.api.rest.dto.response.AuthResponseDTO
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
     */
    suspend fun signUp(signUpRequest: SignUpRequestDTO)

    /**
     * Authenticates an existing user.
     *
     * @param signInRequest Data representing sign-in request details.
     * @return An [AuthResponseDTO] containing user token and profile details upon successful sign-in.
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
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class
    )
    suspend fun getUserProfile(uuid: UUID): UserResponseDTO

    /**
     * Updates the user profile based on the provided user ID and updated information.
     *
     * @param uuid The unique identifier of the user.
     * @param updatedUser Data representing updated user profile details.
     * @return A [UserResponseDTO] containing updated user profile details.
     */
    suspend fun updateUserProfile(uuid: UUID, updatedUser: UpdatedUserRequestDTO): UserResponseDTO
}