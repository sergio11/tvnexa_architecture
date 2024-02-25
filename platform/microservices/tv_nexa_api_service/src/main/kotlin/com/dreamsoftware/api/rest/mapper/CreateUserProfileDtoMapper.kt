package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.request.CreateProfileRequestDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.AvatarType
import com.dreamsoftware.data.database.entity.CreateProfileEntity
import java.util.*

/**
 * Mapper class for mapping a [DataInput] to a [CreateProfileEntity].
 */
class CreateUserProfileDtoMapper : ISimpleMapper<DataInput, CreateProfileEntity> {

    /**
     * Maps a [DataInput] to a [CreateProfileEntity].
     *
     * @param input The input data to be mapped.
     * @return The mapped [CreateProfileEntity].
     */
    override fun map(input: DataInput): CreateProfileEntity = with(input) {
        CreateProfileEntity(
            alias = request.alias,
            pin = request.pin,
            isAdmin = false,
            enableNSFW = request.enableNSFW,
            userId = userId,
            avatarType = runCatching { AvatarType.valueOf(request.avatarType) }.getOrDefault(AvatarType.BOY)
        )
    }

    /**
     * Maps an iterable of [DataInput] to an iterable of [CreateProfileEntity].
     *
     * @param input The iterable of input data to be mapped.
     * @return The iterable of mapped [CreateProfileEntity].
     */
    override fun mapList(input: Iterable<DataInput>): Iterable<CreateProfileEntity> =
        input.map(::map)
}

/**
 * Data class representing input data for creating a user profile.
 *
 * @property request The request containing details for creating the profile.
 * @property userId The unique identifier of the user associated with the profile.
 */
data class DataInput(
    val request: CreateProfileRequestDTO,
    val userId: UUID
)