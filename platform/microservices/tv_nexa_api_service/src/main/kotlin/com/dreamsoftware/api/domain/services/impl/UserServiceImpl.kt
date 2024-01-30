package com.dreamsoftware.api.domain.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.api.domain.repository.IUserRepository
import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.dto.request.SignInRequestDTO
import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.api.rest.dto.request.UpdatedUserRequestDTO
import com.dreamsoftware.api.rest.dto.response.AuthResponseDTO
import com.dreamsoftware.api.rest.dto.response.UserResponseDTO
import com.dreamsoftware.api.rest.utils.getStringProperty
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CreateUserEntity
import com.dreamsoftware.data.database.entity.UpdateUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Implementation of the IUserService interface responsible for managing user-related operations.
 *
 * @property userRepository The repository responsible for user-related data operations.
 * @property profileRepository The repository responsible for profile-related data operations.
 * @property mapper The mapper used to map UserEntity objects to UserResponseDTO objects.
 * @property environment The Ktor application environment for accessing configurations.
 * @property createUserMapper The mapper used to map SignUpRequestDTO to SaveUserEntity.
 * @property updateUserMapper The mapper used to map UpdatedUserRequestDTO to UpdateUserEntity
 */
internal class UserServiceImpl(
    private val userRepository: IUserRepository,
    private val profileRepository: IProfileRepository,
    private val mapper: ISimpleMapper<UserEntity, UserResponseDTO>,
    private val environment: ApplicationEnvironment,
    private val createUserMapper: ISimpleMapper<SignUpRequestDTO, CreateUserEntity>,
    private val updateUserMapper: ISimpleMapper<UpdatedUserRequestDTO, UpdateUserEntity>,
): IUserService {

    private val log = LoggerFactory.getLogger(this::class.java)

    private companion object {
        const val EXPIRATION_TIME_IN_MILLIS = 48 * 60 * 60 * 1000L
    }

    /**
     * Registers a new user based on the provided sign-up request.
     *
     * @param signUpRequest The sign-up request containing user information.
     * @throws AppException.InternalServerError if an internal server error occurs during user registration.
     * @throws AppException.UserAlreadyExistsException if the user already exists.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.UserAlreadyExistsException::class
    )
    override suspend fun signUp(signUpRequest: SignUpRequestDTO): Unit = withContext(Dispatchers.IO) {
        with(userRepository) {
            if(with(signUpRequest) { existsByUsernameOrEmail(username, email) }) {
                throw AppException.UserAlreadyExistsException("User already exists")
            } else {
                try {
                    createUser(createUserMapper.map(signUpRequest))
                } catch (e: Exception) {
                    e.printStackTrace()
                    log.debug("USES (signUp) An exception occurred: ${e.message ?: "Unknown error"}")
                    throw AppException.InternalServerError("An error occurred while user signUp.")
                }
            }
        }
    }

    /**
     * Authenticates a user based on the provided sign-in credentials and generates an authentication token.
     *
     * @param signInRequest The sign-in request containing user credentials.
     * @return An authentication response containing user details and a generated token.
     * @throws AppException.InternalServerError if an internal server error occurs during user sign-in.
     * @throws AppException.InvalidCredentialsException if the provided credentials are invalid.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.InvalidCredentialsException::class
    )
    override suspend fun signIn(signInRequest: SignInRequestDTO): AuthResponseDTO = withContext(Dispatchers.IO) {
        try {
            with(signInRequest) {
                userRepository.signIn(email, password)?.let(mapper::map)?.let { userDTO ->
                    AuthResponseDTO(
                        user = userDTO,
                        profilesCount = profileRepository.countByUser(userDTO.uuid),
                        token =  with(environment.config) {
                            JWT.create()
                                .withSubject(userDTO.uuid)
                                .withAudience(getStringProperty("jwt.audience"))
                                .withIssuer(getStringProperty("jwt.issuer"))
                                .withClaim("username", userDTO.username)
                                .withClaim("firstName", userDTO.firstName)
                                .withClaim("lastName", userDTO.lastName)
                                .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                                .sign(Algorithm.HMAC256(getStringProperty("jwt.secret")))
                        } )
                } ?: throw AppException.InvalidCredentialsException("Invalid credentials!")
            }
        }  catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USES (signIn) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while user signing.")
            } else {
                e
            }
        }
    }

    /**
     * Retrieves a user's profile based on the provided UUID.
     *
     * @param uuid The UUID of the user to retrieve.
     * @return The user's profile information.
     * @throws AppException.InternalServerError if an internal server error occurs while fetching the user profile.
     * @throws AppException.NotFoundException.UserNotFoundException if the user with the given UUID is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class
    )
    override suspend fun getUserProfile(uuid: UUID): UserResponseDTO = withContext(Dispatchers.IO) {
        try {
            userRepository.getUserById(uuid)?.let(mapper::map)
                ?: throw AppException.NotFoundException.UserNotFoundException("User with code '$uuid' not found.")
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USES (getUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding user by UUID.")
            } else {
                e
            }
        }
    }

    /**
     * Updates a user's profile based on the provided UUID and updated user information.
     *
     * @param uuid The UUID of the user to update.
     * @param updatedUser The updated user information.
     * @return The updated user profile information.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.UserAlreadyExistsException::class
    )
    override suspend fun updateUserProfile(uuid: UUID, updatedUser: UpdatedUserRequestDTO): UserResponseDTO = withContext(Dispatchers.IO) {
        try {
            with(userRepository) {
                updatedUser.username?.let { newUsername ->
                    if(existsByUsername(newUsername)) {
                        throw AppException.UserAlreadyExistsException("User already exists")
                    }
                }
                updateUser(uuid, updateUserMapper.map(updatedUser))
                getUserById(uuid)?.let(mapper::map)
                    ?: throw AppException.NotFoundException.UserNotFoundException("User with code '$uuid' not found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USES (updateUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding user by UUID.")
            } else {
                e
            }
        }
    }
}