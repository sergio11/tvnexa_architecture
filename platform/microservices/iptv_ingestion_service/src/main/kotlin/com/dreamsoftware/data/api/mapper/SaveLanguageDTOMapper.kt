package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.model.LanguageDTO

class SaveLanguageDTOMapper: ISimpleMapper<LanguageDTO, SaveLanguageEntity> {
    override fun map(input: LanguageDTO): SaveLanguageEntity = with(input) {
        SaveLanguageEntity(code, name)
    }
    override fun mapList(input: Iterable<LanguageDTO>): Iterable<SaveLanguageEntity> =
        input.map(::map)
}