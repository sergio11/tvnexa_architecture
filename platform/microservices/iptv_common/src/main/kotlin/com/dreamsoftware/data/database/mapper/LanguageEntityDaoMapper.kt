package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.entity.LanguageEntity

class LanguageEntityDaoMapper: IOneSideMapper<LanguageEntityDAO, LanguageEntity> {

    override fun map(input: LanguageEntityDAO): LanguageEntity = with(input) {
        LanguageEntity(code, name)
    }

    override fun mapList(input: Iterable<LanguageEntityDAO>): Iterable<LanguageEntity> =
        input.map(::map)
}