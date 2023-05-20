package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.iptvorg.model.LanguageDTO

class LanguageDTOMapper: IMapper<LanguageDTO, LanguageEntity> {
    override fun map(input: LanguageDTO): LanguageEntity = with(input) {
        LanguageEntity(code, name)
    }
    override fun mapList(input: Iterable<LanguageDTO>): Iterable<LanguageEntity> =
        input.map(::map)
}