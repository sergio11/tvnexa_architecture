package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.entity.SubdivisionTable
import org.jetbrains.exposed.sql.ResultRow

class SubdivisionTableMapper: IOneSideMapper<ResultRow, SubdivisionEntity> {

    override fun map(input: ResultRow): SubdivisionEntity = with(input) {
        SubdivisionEntity(
            code = get(SubdivisionTable.code),
            name = get(SubdivisionTable.name),
            country = get(SubdivisionTable.country)
        )
    }

    override fun mapList(input: Iterable<ResultRow>): Iterable<SubdivisionEntity> =
        input.map(::map)
}