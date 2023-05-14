package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.model.Language

class LanguageMapper: IMapper<LanguageEntity, Language> {

    override fun map(input: LanguageEntity): Language = with(input) {
        Language(code, name)
    }

    override fun mapList(input: Iterable<LanguageEntity>): Iterable<Language> = input.map(::map)

    override fun mapReverse(output: Language): LanguageEntity = with(output) {
        LanguageEntity(code, name)
    }

    override fun mapReverseList(output: Iterable<Language>): Iterable<LanguageEntity> =
        output.map(::mapReverse)
}