package com.dreamsoftware.api.domain.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.IUserRepository
import com.dreamsoftware.api.domain.services.IUserService
import com.dreamsoftware.api.rest.dto.request.SignInRequestDTO
import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.api.rest.dto.request.UpdatedUserRequestDTO
import com.dreamsoftware.api.rest.dto.response.AuthResponseDTO
import com.dreamsoftware.api.rest.dto.response.UserResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveUserEntity
import com.dreamsoftware.data.database.entity.UserEntity
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.*

internal class UserServiceImpl(
    private val userRepository: IUserRepository,
    private val mapper: ISimpleMapper<UserEntity, UserResponseDTO>,
    private val environment: ApplicationEnvironment,
    private val saveUserMapper: ISimpleMapper<SignUpRequestDTO, SaveUserEntity>,
): IUserService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(
        AppException.InternalServerError::class,
        AppException.UserAlreadyExistsException::class
    )
    override suspend fun signUp(signUpRequest: SignUpRequestDTO) = withContext(Dispatchers.IO) {
        userRepository.createUser(saveUserMapper.map(signUpRequest))
    }

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
                        token =  with(environment.config) {
                            JWT.create()
                                .withAudience(property("jwt.audience").getString())
                                .withIssuer(property("jwt.issuer").getString())
                                .withClaim("username", userDTO.username)
                                .withClaim("firstName", userDTO.firstName)
                                .withClaim("lastName", userDTO.lastName)
                                .withExpiresAt(Date(System.currentTimeMillis() + property("jwt.expiration").getString().toLong()))
                                .sign(Algorithm.HMAC256(property("jwt.secret").getString()))
                        } )
                }
                    ?: throw AppException.InvalidCredentialsException("Invalid credentials!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("USES (signIn) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while user signing.")
        }
    }

    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.UserNotFoundException::class
    )
    override suspend fun getUserProfile(uuid: UUID): UserResponseDTO = withContext(Dispatchers.IO) {
        try {
            userRepository.getUserById(uuid)?.let(mapper::map)
                ?: throw AppException.NotFoundException.UserNotFoundException(
                    "User with code '$uuid' not found."
                )
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("USES (getUserProfile) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding user by UUID.")
        }
    }

    override suspend fun updateUserProfile(uuid: UUID, updatedUser: UpdatedUserRequestDTO): UserResponseDTO = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }
}