package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.response.UserResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.UserEntity

/**
 * Implementation of the ISimpleMapper interface responsible for mapping UserEntity objects
 * to UserResponseDTO objects.
 */
class UserResponseDtoMapper : ISimpleMapper<UserEntity, UserResponseDTO> {

    /**
     * Maps a UserEntity object to a UserResponseDTO object.
     *
     * @param input The UserEntity object to be mapped.
     * @return The mapped UserResponseDTO object.
     */
    override fun map(input: UserEntity): UserResponseDTO = with(input) {
        UserResponseDTO(
            uuid = uuid.toString(),
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }

    /**
     * Maps an Iterable of UserEntity objects to an Iterable of UserResponseDTO objects.
     *
     * @param input The Iterable of UserEntity objects to be mapped.
     * @return The Iterable of mapped UserResponseDTO objects.
     */
    override fun mapList(input: Iterable<UserEntity>): Iterable<UserResponseDTO> =
        input.map(::map)
}
