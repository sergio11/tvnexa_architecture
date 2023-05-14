package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CategoryTable
import org.jetbrains.exposed.sql.ResultRow

class CategoryTableMapper: IOneSideMapper<ResultRow, CategoryEntity> {

    override fun map(input: ResultRow): CategoryEntity = with(input) {
        CategoryEntity(
            id = get(CategoryTable.id).value,
            name = get(CategoryTable.name)
        )
    }

    override fun mapList(input: Iterable<ResultRow>): Iterable<CategoryEntity> =
        input.map(::map)
}