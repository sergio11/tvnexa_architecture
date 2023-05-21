package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.model.LanguageDTO

class SaveLanguageDTOMapper: IMapper<LanguageDTO, SaveLanguageEntity> {
    override fun map(input: LanguageDTO): SaveLanguageEntity = with(input) {
        SaveLanguageEntity(code, name)
    }
    override fun mapList(input: Iterable<LanguageDTO>): Iterable<SaveLanguageEntity> =
        input.map(::map)
}