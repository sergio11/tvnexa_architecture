package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import com.dreamsoftware.data.iptvorg.model.CategoryDTO

/**
 * Mapper class for converting [CategoryDTO] objects to [SaveCategoryEntity] objects.
 * This mapper is responsible for transforming data transfer objects (DTOs) received from
 * a network source into entities that can be stored in a database.
 */
class SaveCategoryDTOMapper : ISimpleMapper<CategoryDTO, SaveCategoryEntity> {

    /**
     * Maps a single [CategoryDTO] object to a [SaveCategoryEntity] object.
     *
     * @param input The input [CategoryDTO] object to be mapped.
     * @return A [SaveCategoryEntity] object containing the mapped data.
     */
    override fun map(input: CategoryDTO): SaveCategoryEntity = with(input) {
        // Create a new SaveCategoryEntity object using the id and name properties from CategoryDTO.
        SaveCategoryEntity(id, name)
    }

    /**
     * Maps a collection of [CategoryDTO] objects to a collection of [SaveCategoryEntity] objects.
     *
     * @param input The iterable collection of [CategoryDTO] objects to be mapped.
     * @return An iterable collection of [SaveCategoryEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<CategoryDTO>): Iterable<SaveCategoryEntity> =
        input.map(::map)
}