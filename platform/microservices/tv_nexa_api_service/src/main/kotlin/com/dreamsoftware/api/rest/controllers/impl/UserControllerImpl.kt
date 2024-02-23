package com.dreamsoftware.api.rest.controllers.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IChannelRepository
import com.dreamsoftware.api.domain.repository.IProfileRepository
import com.dreamsoftware.api.domain.repository.IUserRepository
import com.dreamsoftware.api.rest.controllers.IUserController
import com.dreamsoftware.api.rest.controllers.impl.core.SupportController
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
import kotlinx.coroutines.CoroutineScope
import java.util.*

/**
 * Implementation of the IUserController interface responsible for managing user-related operations.
 *
 * @property userRepository The repository responsible for user-related data operations.
 * @property profileRepository The repository responsible for profile-related data operations.
 * @property channelRepository the repository responsible for channel-related data operations.
 * @property mapper The mapper used to map [UserEntity] objects to [UserResponseDTO] objects.
 * @property profileMapper The mapper used to map [ProfileEntity] objects to [ProfileResponseDTO] objects.
 * @property updateProfileMapper The mapper used to map [UpdatedProfileRequestDTO] to [UpdateProfileEntity].
 * @property environment The Ktor application environment for accessing configurations.
 * @property createUserMapper The mapper used to map [SignUpRequestDTO] to [CreateUserEntity].
 * @property updateUserMapper The mapper used to map [UpdatedUserRequestDTO] to [UpdateUserEntity].
 * @property createUserProfileMapper The mapper used to map [DataInput] to [CreateProfileEntity].
 * @property channelMapper The mapper used to map [SimpleChannelEntity] to [SimpleChannelResponseDTO].
 */
internal class UserControllerImpl(
    private val userRepository: IUserRepository,
    private val profileRepository: IProfileRepository,
    private val channelRepository: IChannelRepository,
    private val mapper: ISimpleMapper<UserEntity, UserResponseDTO>,
    private val profileMapper: ISimpleMapper<ProfileEntity, ProfileResponseDTO>,
    private val updateProfileMapper: ISimpleMapper<UpdatedProfileRequestDTO, UpdateProfileEntity>,
    private val environment: ApplicationEnvironment,
    private val createUserMapper: ISimpleMapper<SignUpRequestDTO, CreateUserEntity>,
    private val updateUserMapper: ISimpleMapper<UpdatedUserRequestDTO, UpdateUserEntity>,
    private val createUserProfileMapper: ISimpleMapper<DataInput, CreateProfileEntity>,
    private val channelMapper: ISimpleMapper<SimpleChannelEntity, SimpleChannelResponseDTO>
): SupportController(), IUserController {

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
    override suspend fun getUserProfiles(uuid: UUID): List<ProfileResponseDTO> =
        safeCall(errorMessage = "An error occurred while finding profiles by user.") {
            if(!userRepository.existsById(uuid)) {
                throw AppException.NotFoundException.UserNotFoundException("User $uuid not found")
            }
            profileRepository.findByUser(uuid).map(profileMapper::map)
        }

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
    override suspend fun getUserProfileDetail(userUuid: UUID, profileUUID: UUID): ProfileResponseDTO =
        safeCall(errorMessage = "An error occurred while finding the profile") {
            if(!userRepository.existsById(userUuid)) {
                throw AppException.NotFoundException.UserNotFoundException("User $userUuid not found")
            }
            if(!profileRepository.canBeManagedByUser(profileUUID, userUuid)) {
                throw AppException.NotFoundException.UserNotAllowedException("User $userUuid are not allowed to manage this profile")
            }
            profileRepository.getProfileById(profileUUID)?.let(profileMapper::map)
                ?: throw AppException.NotFoundException.ProfileNotFoundException("Profile with code '$profileUUID' not found.")
        }

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
    override suspend fun updateUserProfile(
        userUuid: UUID,
        profileUUID: UUID,
        data: UpdatedProfileRequestDTO
    ): ProfileResponseDTO =
        safeCall(errorMessage = "An error occurred while finding profiles by user.") {
            with(profileRepository) {
                if(!userRepository.existsById(userUuid)) {
                    throw AppException.NotFoundException.UserNotFoundException("User $userUuid not found")
                }
                if(!canBeManagedByUser(profileUUID, userUuid)) {
                    throw AppException.NotFoundException.UserNotAllowedException("User $userUuid are not allowed to manage this profile")
                }
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
    override suspend fun deleteUserProfile(userUuid: UUID, profileUUID: UUID): Unit =
        safeCall(errorMessage = "An error occurred while deleting profile.") {
            profileRepository.deleteProfile(profileUUID)
        }


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
    override suspend fun createProfile(userUuid: UUID, data: CreateProfileRequestDTO): Unit =
        safeCall(errorMessage = "An error occurred while deleting profile.") {
            if(!userRepository.existsById(userUuid)) {
                throw AppException.NotFoundException.UserNotFoundException("User $userUuid not found")
            }
            if(profileRepository.hasUserProfileLimitReached(userUuid)) {
                throw AppException.UserProfilesLimitReachedException("The user $userUuid has reached the limit of allowed profiles.")
            }
            profileRepository.createProfile(createUserProfileMapper.map(DataInput(data, userUuid)))
        }

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
    override suspend fun verifyPin(userUuid: UUID, profileUUID: UUID, data: PinVerificationRequestDTO): Boolean =
        safeCall(errorMessage = "An error occurred while verifying pin.") {
            if(!userRepository.existsById(userUuid)) {
                throw AppException.NotFoundException.UserNotFoundException("User $userUuid not found")
            }
            if(!profileRepository.canBeManagedByUser(profileUUID, userUuid)) {
                throw AppException.NotFoundException.UserNotAllowedException("User $userUuid are not allowed to manage this profile")
            }
            profileRepository.verifyPin(profileUUID, data.pin)
        }

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
    override suspend fun getBlockedChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO> =
        safeProfileManagementCall(
            errorMessage = "An error occurred while fetching blocked channels.",
            userUuid = userUuid,
            profileUUID = profileUUID
        ) {
            // Fetch blocked channels from the profile repository
            profileRepository.getBlockedChannels(profileUUID)
                // Map the retrieved channels to SimpleChannelResponseDTO using the channelMapper
                .map(channelMapper::map)
        }

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
    override suspend fun saveBlockedChannel(userUuid: UUID, profileUUID: UUID, channelId: String) {
        safeProfileManagementCall(
            errorMessage = "An error occurred while saving blocked channel",
            userUuid = userUuid,
            profileUUID = profileUUID,
            channelId = channelId
        ) {
            profileRepository.saveBlockedChannel(profileUUID, channelId)
        }
    }

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
    override suspend fun deleteBlockedChannel(userUuid: UUID, profileUUID: UUID, channelId: String) {
        safeProfileManagementCall(
            errorMessage = "An error occurred while deleting channel from blocked list",
            userUuid = userUuid,
            profileUUID = profileUUID,
            channelId = channelId
        ) {
            profileRepository.deleteBlockedChannel(profileUUID, channelId)
        }
    }


    /**
     * Retrieves the list of favorite channels for a given user profile.
     * @param userUuid The UUID of the user requesting the favorite channels.
     * @param profileUUID The UUID of the profile for which favorite channels are being fetched.
     * @return A list of SimpleChannelResponseDTO representing the favorite channels.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ProfileNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    override suspend fun getFavoriteChannels(userUuid: UUID, profileUUID: UUID): List<SimpleChannelResponseDTO> =
        safeProfileManagementCall(
            errorMessage = "An error occurred while fetching favorite channels.",
            userUuid = userUuid,
            profileUUID = profileUUID
        ) {
            // Fetch favorite channels from the profile repository
            profileRepository.getFavoriteChannels(profileUUID)
                // Map the retrieved channels to SimpleChannelResponseDTO using the channelMapper
                .map(channelMapper::map)
        }

    /**
     * Saves a channel as a favorite for a given user profile.
     * @param userUuid The UUID of the user associated with the operation.
     * @param profileUUID The UUID of the profile for which the channel will be saved as a favorite.
     * @param channelId The ID of the channel to be saved as a favorite.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.ProfileNotFoundException::class,
        AppException.NotFoundException.ChannelNotFoundException::class,
        AppException.NotFoundException.UserNotFoundException::class,
        AppException.NotFoundException.UserNotAllowedException::class,
    )
    override suspend fun saveFavoriteChannel(userUuid: UUID, profileUUID: UUID, channelId: String) {
        safeProfileManagementCall(
            errorMessage = "An error occurred while saving favorite channel",
            userUuid = userUuid,
            profileUUID = profileUUID,
            channelId = channelId
        ) {
            profileRepository.saveFavoriteChannel(profileUUID = profileUUID, channelId = channelId)
        }
    }

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
    override suspend fun deleteFavoriteChannel(userUuid: UUID, profileUUID: UUID, channelId: String) {
        safeProfileManagementCall(
            errorMessage = "An error occurred while remove channel from favorites",
            userUuid = userUuid,
            profileUUID = profileUUID,
            channelId = channelId
        ) {
            profileRepository.deleteFavoriteChannel(profileUUID = profileUUID, channelId = channelId)
        }
    }

    /**
     * Executes a block of code after performing profile management related checks.
     * @param errorMessage The error message to be used if an error occurs during the operation.
     * @param userUuid The UUID of the user associated with the operation.
     * @param profileUUID The UUID of the profile associated with the operation.
     * @param channelId The ID of the channel associated with the operation.
     * @param block The block of code to be executed if all checks pass.
     * @throws AppException.NotFoundException.UserNotFoundException If the specified user is not found.
     * @throws AppException.NotFoundException.ProfileNotFoundException If the specified profile is not found.
     * @throws AppException.NotFoundException.UserNotAllowedException If the specified user is not allowed to perform the operation.
     * @throws AppException.NotFoundException.ChannelNotFoundException If the specified channel is not found.
     * @throws AppException.InternalServerError If an internal server error occurs during the operation.
     */
    private suspend fun <T>safeProfileManagementCall(
        errorMessage: String,
        userUuid: UUID,
        profileUUID: UUID,
        channelId: String? = null,
        block: suspend CoroutineScope.() -> T
    ) = safeCall(errorMessage) {
        if(!userRepository.existsById(userUuid)) {
            throw AppException.NotFoundException.UserNotFoundException("User $userUuid not found")
        }
        if(!profileRepository.existsById(profileUUID)) {
            throw AppException.NotFoundException.ProfileNotFoundException("Profile $profileUUID not found")
        }
        if(!profileRepository.canBeManagedByUser(profileUUID, userUuid)) {
            throw AppException.NotFoundException.UserNotAllowedException("User $userUuid are not allowed to manage this profile")
        }
        channelId?.let {
            if(!channelRepository.existsById(it)) {
                throw AppException.NotFoundException.ChannelNotFoundException("Channel $channelId not found")
            }
        }
        block()
    }
}