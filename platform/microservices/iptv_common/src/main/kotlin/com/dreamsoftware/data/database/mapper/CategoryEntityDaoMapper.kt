package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity

/**
 * Mapper for converting [CategoryEntityDAO] objects to [CategoryEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([CategoryEntityDAO])
 * to the domain representation ([CategoryEntity]) of a category.
 */
class CategoryEntityDaoMapper : ISimpleMapper<CategoryEntityDAO, CategoryEntity> {

    /**
     * Maps a single [CategoryEntityDAO] object to a [CategoryEntity] object.
     *
     * @param input The input [CategoryEntityDAO] to map.
     * @return The mapped [CategoryEntity].
     */
    override fun map(input: CategoryEntityDAO): CategoryEntity = with(input) {
        CategoryEntity(
            id = categoryId,
            name = name
        )
    }

    /**
     * Maps a list of [CategoryEntityDAO] objects to a list of [CategoryEntity] objects.
     *
     * @param input The input list of [CategoryEntityDAO] objects to map.
     * @return The mapped list of [CategoryEntity] objects.
     */
    override fun mapList(input: Iterable<CategoryEntityDAO>): Iterable<CategoryEntity> =
        input.map(::map)
}