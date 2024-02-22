package com.dreamsoftware.api.domain.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.api.domain.repository.IUserRepository
import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.request.*
import com.dreamsoftware.api.rest.dto.response.AuthResponseDTO
import com.dreamsoftware.api.rest.dto.response.ProfileResponseDTO
import com.dreamsoftware.api.rest.dto.response.SimpleChannelResponseDTO
import com.dreamsoftware.api.rest.dto.response.UserResponseDTO
import com.dreamsoftware.api.rest.mapper.DataInput
import com.dreamsoftware.api.rest.utils.getStringProperty
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.core.toUUID
import com.dreamsoftware.data.database.dao.AvatarType
import com.dreamsoftware.data.database.entity.*
import io.ktor.server.application.*
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
): SupportService(), IUserService {

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
    override suspend fun signUp(signUpRequest: SignUpRequestDTO): Unit =
        safeCall(errorMessage = "An error occurred while user signUp.") {
            with(userRepository) {
                if(with(signUpRequest) { existsByUsernameOrEmail(username, email) }) {
                    throw AppException.UserAlreadyExistsException("User already exists")
                } else {
                    createUser(createUserMapper.map(signUpRequest))
                    getUserByUsername(signUpRequest.username)?.let {
                        profileRepository.createProfile(
                            data = CreateProfileEntity(
                                alias = it.firstName,
                                pin = DEFAULT_ADMIN_PIN,
                                userId = it.uuid,
                                isAdmin = true,
                                enableNSFW = true,
                                avatarType = AvatarType.BOY
                            )
                        )
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
    override suspend fun signIn(signInRequest: SignInRequestDTO): AuthResponseDTO =
        safeCall(errorMessage = "An error occurred while user signing.") {
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
    override suspend fun getUserDetail(uuid: UUID): UserResponseDTO =
        safeCall(errorMessage = "An error occurred while finding user by UUID.") {
            userRepository.getUserById(uuid)?.let(mapper::map)
                ?: throw AppException.NotFoundException.UserNotFoundException("User with code '$uuid' not found.")
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
    override suspend fun getUserProfiles(uuid: UUID): List<ProfileResponseDTO> =
        safeCall(errorMessage = "An error occurred while finding profiles by user.") {
            profileRepository.findByUser(uuid).map(profileMapper::map)
        }

    /**
     * Retrieves the user profile details for the specified profile.
     *
     * @param userUuid The UUID of the user for whom the profile is being retrieved.
     * @param profileUUID The UUID of the profile to retrieve.
     * @return A [ProfileResponseDTO] object containing the profile details.
     * @throws AppException.InternalServerError If an internal application error occurs during the process.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ProfileNotFoundException::class
    )
    override suspend fun getUserProfileDetail(userUuid: UUID, profileUUID: UUID): ProfileResponseDTO =
        safeCall(errorMessage = "An error occurred while finding the profile") {
            profileRepository.getProfileById(profileUUID)?.let(profileMapper::map)
                ?: throw AppException.NotFoundException.ProfileNotFoundException("Profile with code '$profileUUID' not found.")
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
    ): ProfileResponseDTO =
        safeCall(errorMessage = "An error occurred while finding profiles by user.") {
            with(profileRepository) {
                update(profileUUID, updateProfileMapper.map(data))
                getProfileById(profileUUID)?.let(profileMapper::map)
                    ?: throw AppException.NotFoundException.ProfileNotFoundException("Profile with code '$profileUUID' not found.")
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
    override suspend fun updateUserDetail(uuid: UUID, updatedUser: UpdatedUserRequestDTO): UserResponseDTO =
        safeCall(errorMessage = "An error occurred while finding user by UUID.") {
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
    override suspend fun deleteUserProfile(userUuid: UUID, profileUUID: UUID): Unit =
        safeCall(errorMessage = "An error occurred while deleting profile.") {
            profileRepository.deleteProfile(profileUUID)
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
    override suspend fun createProfile(userUuid: UUID, data: CreateProfileRequestDTO): Unit =
        safeCall(errorMessage = "An error occurred while deleting profile.") {
            profileRepository.createProfile(createUserProfileMapper.map(DataInput(data, userUuid)))
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
    override suspend fun verifyPin(userUuid: UUID, profileUUID: UUID, data: PinVerificationRequestDTO): Boolean =
        safeCall(errorMessage = "An error occurred while verifying pin.") {
            profileRepository.verifyPin(profileUUID, data.pin)
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
    override suspend fun getBlockedChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching blocked channels.") {
            // Fetch blocked channels from the profile repository
            profileRepository.getBlockedChannels(profileUUID)
                // Map the retrieved channels to SimpleChannelResponseDTO using the channelMapper
                .map(channelMapper::map)
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
    override suspend fun getFavoriteChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching favorite channels.") {
                // Fetch favorite channels from the profile repository
                profileRepository.getFavoriteChannels(profileUUID)
                // Map the retrieved channels to SimpleChannelResponseDTO using the channelMapper
                .map(channelMapper::map)
        }
}