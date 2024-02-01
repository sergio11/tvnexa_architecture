package com.dreamsoftware.api.domain.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.api.domain.repository.IUserRepository
import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.dto.request.*
import com.dreamsoftware.api.rest.dto.response.AuthResponseDTO
import com.dreamsoftware.api.rest.dto.response.ProfileResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO
import com.dreamsoftware.api.rest.dto.response.UserResponseDTO
import com.dreamsoftware.api.rest.mapper.DataInput
import com.dreamsoftware.api.rest.utils.getStringProperty
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.core.toUUID
import com.dreamsoftware.data.database.dao.ProfileType
import com.dreamsoftware.data.database.entity.*
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
 * @property mapper The mapper used to map [UserEntity] objects to [UserResponseDTO] objects.
 * @property profileMapper The mapper used to map [ProfileEntity] objects to [ProfileResponseDTO] objects.
 * @property updateProfileMapper The mapper used to map [UpdatedProfileRequestDTO] to [UpdateProfileEntity].
 * @property environment The Ktor application environment for accessing configurations.
 * @property createUserMapper The mapper used to map [SignUpRequestDTO] to [CreateUserEntity].
 * @property updateUserMapper The mapper used to map [UpdatedUserRequestDTO] to [UpdateUserEntity].
 * @property createUserProfileMapper The mapper used to map [DataInput] to [CreateProfileEntity].
 * @property channelMapper The mapper used to map [SimpleChannelEntity] to [SimpleChannelResponseDTO].
 */
internal class UserServiceImpl(
    private val userRepository: IUserRepository,
    private val profileRepository: IProfileRepository,
    private val mapper: ISimpleMapper<UserEntity, UserResponseDTO>,
    private val profileMapper: ISimpleMapper<ProfileEntity, ProfileResponseDTO>,
    private val updateProfileMapper: ISimpleMapper<UpdatedProfileRequestDTO, UpdateProfileEntity>,
    private val environment: ApplicationEnvironment,
    private val createUserMapper: ISimpleMapper<SignUpRequestDTO, CreateUserEntity>,
    private val updateUserMapper: ISimpleMapper<UpdatedUserRequestDTO, UpdateUserEntity>,
    private val createUserProfileMapper: ISimpleMapper<DataInput, CreateProfileEntity>,
    private val channelMapper: ISimpleMapper<SimpleChannelEntity, SimpleChannelResponseDTO>
): IUserService {

    private val log = LoggerFactory.getLogger(this::class.java)

    private companion object {
        const val EXPIRATION_TIME_IN_MILLIS = 48 * 60 * 60 * 1000L
        const val DEFAULT_ADMIN_PIN = 123456
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
                    getUserByUsername(signUpRequest.username)?.let {
                        profileRepository.createProfile(
                            data = CreateProfileEntity(
                                alias = it.firstName,
                                pin = DEFAULT_ADMIN_PIN,
                                userId = it.uuid,
                                isAdmin = true,
                                type = ProfileType.BOY
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    log.debug("USER (signUp) An exception occurred: ${e.message ?: "Unknown error"}")
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
                        profilesCount = profileRepository.countByUser(userDTO.uuid.toUUID()),
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
                log.debug("USER (signIn) An exception occurred: ${e.message ?: "Unknown error"}")
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
    override suspend fun getUserDetail(uuid: UUID): UserResponseDTO = withContext(Dispatchers.IO) {
        try {
            userRepository.getUserById(uuid)?.let(mapper::map)
                ?: throw AppException.NotFoundException.UserNotFoundException("User with code '$uuid' not found.")
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (getUserDetail) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding user by UUID.")
            } else {
                e
            }
        }
    }

    /**
     * Retrieves profiles associated with a user identified by their UUID.
     *
     * @param uuid The unique identifier (UUID) of the user.
     * @return A list of [ProfileResponseDTO] objects representing the user's profiles.
     *
     * @throws AppException.InternalServerError If there is an internal server error during profile retrieval.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun getUserProfiles(uuid: UUID): List<ProfileResponseDTO> = withContext(Dispatchers.IO) {
        try {
            profileRepository.findByUser(uuid).map(profileMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (getUserProfiles) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding profiles by user.")
            } else {
                e
            }
        }
    }

    /**
     * Updates a user's profile based on the provided data.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the profile to be updated.
     * @param data The data containing the updates to be applied to the profile.
     * @return A [ProfileResponseDTO] representing the updated user profile.
     * @throws AppException.InternalServerError if an unexpected internal error occurs.
     * @throws AppException.NotFoundException.ProfileNotFoundException if the specified profile is not found.
     * @throws AppException.UserProfileAlreadyExistsException if there is an attempt to create a duplicate profile.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ProfileNotFoundException::class,
        AppException.UserProfileAlreadyExistsException::class
    )
    override suspend fun updateUserProfile(
        userUuid: UUID,
        profileUUID: UUID,
        data: UpdatedProfileRequestDTO
    ): ProfileResponseDTO = withContext(Dispatchers.IO) {
        try {
            with(profileRepository) {
                update(profileUUID, updateProfileMapper.map(data))
                getProfileById(profileUUID)?.let(profileMapper::map)
                    ?: throw AppException.NotFoundException.ProfileNotFoundException("Profile with code '$profileUUID' not found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (updateUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding profiles by user.")
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
    override suspend fun updateUserDetail(uuid: UUID, updatedUser: UpdatedUserRequestDTO): UserResponseDTO = withContext(Dispatchers.IO) {
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
                log.debug("USER (updateUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while finding user by UUID.")
            } else {
                e
            }
        }
    }

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
    override suspend fun deleteUserProfile(userUuid: UUID, profileUUID: UUID): Unit = withContext(Dispatchers.IO) {
        try {
            profileRepository.deleteProfile(profileUUID)
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (deleteUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while deleting profile.")
            } else {
                e
            }
        }
    }

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
    override suspend fun createProfile(userUuid: UUID, data: CreateProfileRequestDTO): Unit = withContext(Dispatchers.IO) {
        try {
            profileRepository.createProfile(createUserProfileMapper.map(DataInput(data, userUuid)))
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (deleteUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while deleting profile.")
            } else {
                e
            }
        }
    }

    /**
     * Verifies the PIN of a user's profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the profile to be verified.
     * @param data The PIN to be verified.
     * @return `true` if the PIN is verified successfully, `false` otherwise.
     * @throws [AppException.NotFoundException.ProfileNotFoundException] if the specified profile is not found.
     */
    @Throws(
        AppException.NotFoundException.ProfileNotFoundException::class
    )
    override suspend fun verifyPin(userUuid: UUID, profileUUID: UUID, data: PinVerificationRequestDTO): Boolean = withContext(Dispatchers.IO) {
        try {
            profileRepository.verifyPin(profileUUID, data.pin)
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (verifyPin) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while verifying pin.")
            } else {
                e
            }
        }
    }

    /**
     * Retrieves a list of blocked channels for the specified user profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the user profile.
     * @return A list of [SimpleChannelResponseDTO] objects representing the blocked channels.
     * @throws AppException.InternalServerError if there is an internal server error during the operation.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun getBlockedChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO> = withContext(Dispatchers.IO) {
        try {
            // Fetch blocked channels from the profile repository
            profileRepository.getBlockedChannels(profileUUID)
                // Map the retrieved channels to SimpleChannelResponseDTO using the channelMapper
                .map(channelMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (getBlockedChannels) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while fetching blocked channels.")
            } else {
                e
            }
        }
    }

    /**
     * Retrieves a list of favorite channels for the specified user profile.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUUID The unique identifier of the user profile.
     * @return A list of [SimpleChannelResponseDTO] objects representing the favorite channels.
     * @throws AppException.InternalServerError if there is an internal server error during the operation.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun getFavoriteChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO> = withContext(Dispatchers.IO) {
        try {
            // Fetch favorite channels from the profile repository
            profileRepository.getFavoriteChannels(profileUUID)
                // Map the retrieved channels to SimpleChannelResponseDTO using the channelMapper
                .map(channelMapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            throw if (e !is AppException) {
                log.debug("USER (getFavoriteChannels) An exception occurred: ${e.message ?: "Unknown error"}")
                AppException.InternalServerError("An error occurred while fetching favorite channels.")
            } else {
                e
            }
        }
    }
}