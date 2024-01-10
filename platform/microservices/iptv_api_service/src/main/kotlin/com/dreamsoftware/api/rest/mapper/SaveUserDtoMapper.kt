package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveUserEntity

/**
 * Implementation of the ISimpleMapper interface responsible for mapping SignUpRequestDTO objects
 * to SaveUserEntity objects.
 */
class SaveUserDtoMapper : ISimpleMapper<SignUpRequestDTO, SaveUserEntity> {

    /**
     * Maps a SignUpRequestDTO object to a SaveUserEntity object.
     *
     * @param input The SignUpRequestDTO object to be mapped.
     * @return The mapped SaveUserEntity object.
     */
    override fun map(input: SignUpRequestDTO): SaveUserEntity = with(input) {
        SaveUserEntity(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }

    /**
     * Maps an Iterable of SignUpRequestDTO objects to an Iterable of SaveUserEntity objects.
     *
     * @param input The Iterable of SignUpRequestDTO objects to be mapped.
     * @return The Iterable of mapped SaveUserEntity objects.
     */
    override fun mapList(input: Iterable<SignUpRequestDTO>): Iterable<SaveUserEntity> =
        input.map(::map)
}