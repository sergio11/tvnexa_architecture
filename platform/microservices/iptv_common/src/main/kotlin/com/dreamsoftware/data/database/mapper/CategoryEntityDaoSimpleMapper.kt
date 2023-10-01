package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity

class CategoryEntityDaoSimpleMapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity> {

    override fun map(input: CategoryEntityDAO): CategoryEntity = with(input) {
        CategoryEntity(
            id = categoryId,
            name = name
        )
    }

    override fun mapList(input: Iterable<CategoryEntityDAO>): Iterable<CategoryEntity> =
        input.map(::map)
}