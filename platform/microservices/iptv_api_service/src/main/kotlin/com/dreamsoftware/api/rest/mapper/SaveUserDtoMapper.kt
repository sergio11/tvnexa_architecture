package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveUserEntity

class SaveUserDtoMapper : ISimpleMapper<SignUpRequestDTO, SaveUserEntity> {

    override fun map(input: SignUpRequestDTO): SaveUserEntity = with(input) {
        SaveUserEntity(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }

    override fun mapList(input: Iterable<SignUpRequestDTO>): Iterable<SaveUserEntity> =
        input.map(::map)
}
