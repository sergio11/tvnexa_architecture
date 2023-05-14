package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.LanguageTable
import org.jetbrains.exposed.sql.ResultRow

class LanguageTableMapper: IOneSideMapper<ResultRow, LanguageEntity> {

    override fun map(input: ResultRow): LanguageEntity = with(input) {
        LanguageEntity(
            code = get(LanguageTable.code),
            name = get(LanguageTable.name)
        )
    }

    override fun mapList(input: Iterable<ResultRow>): Iterable<LanguageEntity> =
        input.map(::map)
}