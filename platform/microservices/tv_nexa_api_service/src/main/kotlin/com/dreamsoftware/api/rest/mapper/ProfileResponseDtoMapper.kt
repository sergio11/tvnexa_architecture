package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.response.ProfileResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.ProfileEntity

/**
 * Mapper class for mapping [ProfileEntity] objects to [ProfileResponseDTO] objects.
 */
class ProfileResponseDtoMapper : ISimpleMapper<ProfileEntity, ProfileResponseDTO> {

    /**
     * Maps a single [ProfileEntity] to a [ProfileResponseDTO].
     *
     * @param input The input [ProfileEntity] to be mapped.
     * @return The mapped [ProfileResponseDTO].
     */
    override fun map(input: ProfileEntity): ProfileResponseDTO = with(input) {
        ProfileResponseDTO(
            uuid = uuid.toString(),
            alias = alias,
            isAdmin = isAdmin,
            type = type.name
        )
    }

    /**
     * Maps a list of [ProfileEntity] objects to a list of [ProfileResponseDTO] objects.
     *
     * @param input The input list of [ProfileEntity] objects to be mapped.
     * @return The mapped list of [ProfileResponseDTO] objects.
     */
    override fun mapList(input: Iterable<ProfileEntity>): Iterable<ProfileResponseDTO> =
        input.map(::map)
}
