package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.request.UpdatedProfileRequestDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.ProfileType
import com.dreamsoftware.data.database.entity.UpdateProfileEntity

/**
 * Mapper class for converting [UpdatedProfileRequestDTO] objects to [UpdateProfileEntity] objects.
 *
 * This class implements the [ISimpleMapper] interface to provide mapping functionality for updating user profiles.
 */
class UpdateUserProfileDtoMapper : ISimpleMapper<UpdatedProfileRequestDTO, UpdateProfileEntity> {

    /**
     * Maps a single [UpdatedProfileRequestDTO] to an [UpdateProfileEntity].
     *
     * @param input The [UpdatedProfileRequestDTO] to be mapped.
     * @return An [UpdateProfileEntity] object representing the mapped data.
     */
    override fun map(input: UpdatedProfileRequestDTO): UpdateProfileEntity = with(input) {
        UpdateProfileEntity(
            alias = alias,
            pin = pin,
            isAdmin = isAdmin,
            type = type?.let { runCatching { ProfileType.valueOf(it) }.getOrNull() }
        )
    }

    /**
     * Maps an iterable collection of [UpdatedProfileRequestDTO] to an iterable collection of [UpdateProfileEntity].
     *
     * @param input The iterable collection of [UpdatedProfileRequestDTO] to be mapped.
     * @return An iterable collection of [UpdateProfileEntity] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<UpdatedProfileRequestDTO>): Iterable<UpdateProfileEntity> =
        input.map(::map)
}