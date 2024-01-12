package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CreateUserEntity

/**
 * Implementation of the ISimpleMapper interface responsible for mapping SignUpRequestDTO objects
 * to CreateUserEntity objects.
 */
class SaveUserDtoMapper : ISimpleMapper<SignUpRequestDTO, CreateUserEntity> {

    /**
     * Maps a SignUpRequestDTO object to a SaveUserEntity object.
     *
     * @param input The SignUpRequestDTO object to be mapped.
     * @return The mapped CreateUserEntity object.
     */
    override fun map(input: SignUpRequestDTO): CreateUserEntity = with(input) {
        CreateUserEntity(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }

    /**
     * Maps an Iterable of SignUpRequestDTO objects to an Iterable of CreateUserEntity objects.
     *
     * @param input The Iterable of SignUpRequestDTO objects to be mapped.
     * @return The Iterable of mapped CreateUserEntity objects.
     */
    override fun mapList(input: Iterable<SignUpRequestDTO>): Iterable<CreateUserEntity> =
        input.map(::map)
}