package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.request.UpdatedUserRequestDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.UpdateUserEntity

/**
 * Implementation of the [ISimpleMapper] interface to map instances of [UpdatedUserRequestDTO] to [UpdateUserEntity].
 *
 * This mapper is specifically designed for updating user profiles, mapping relevant fields from the DTO to the entity.
 *
 * @see [ISimpleMapper]
 * @see [UpdatedUserRequestDTO]
 * @see [UpdateUserEntity]
 *
 * @constructor Creates an instance of the [UpdateUserDtoMapper] class.
 */
class UpdateUserDtoMapper : ISimpleMapper<UpdatedUserRequestDTO, UpdateUserEntity> {

    /**
     * Maps a single instance of [UpdatedUserRequestDTO] to [UpdateUserEntity].
     *
     * @param input The input [UpdatedUserRequestDTO] to be mapped.
     * @return The resulting [UpdateUserEntity] containing the mapped fields.
     */
    override fun map(input: UpdatedUserRequestDTO): UpdateUserEntity = with(input) {
        UpdateUserEntity(
            username = username,
            firstName = firstName,
            lastName = lastName
        )
    }

    /**
     * Maps an iterable collection of [UpdatedUserRequestDTO] instances to an iterable collection of [UpdateUserEntity] instances.
     *
     * @param input The iterable collection of [UpdatedUserRequestDTO] instances to be mapped.
     * @return The resulting iterable collection of [UpdateUserEntity] instances containing the mapped fields.
     */
    override fun mapList(input: Iterable<UpdatedUserRequestDTO>): Iterable<UpdateUserEntity> =
        input.map(::map)
}