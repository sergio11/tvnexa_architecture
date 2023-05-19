package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity

class CategoryEntityDaoMapper: IOneSideMapper<CategoryEntityDAO, CategoryEntity> {

    override fun map(input: CategoryEntityDAO): CategoryEntity = with(input) {
        CategoryEntity(
            id = categoryId,
            name = name
        )
    }

    override fun mapList(input: Iterable<CategoryEntityDAO>): Iterable<CategoryEntity> =
        input.map(::map)
}