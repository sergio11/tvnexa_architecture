package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.CategoryResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CategoryEntity

/**
 * Mapper class that maps [CategoryEntity] objects to [CategoryResponseDTO] objects.
 */
class CategoryResponseDtoMapper : ISimpleMapper<CategoryEntity, CategoryResponseDTO> {

    /**
     * Map a single [CategoryEntity] object to a [CategoryResponseDTO] object.
     *
     * @param input The [CategoryEntity] object to be mapped.
     * @return A [CategoryResponseDTO] object representing the mapped data.
     */
    override fun map(input: CategoryEntity): CategoryResponseDTO =
        CategoryResponseDTO(
            id = input.id,
            name = input.name
        )

    /**
     * Map a collection of [CategoryEntity] objects to a collection of [CategoryResponseDTO] objects.
     *
     * @param input The collection of [CategoryEntity] objects to be mapped.
     * @return A collection of [CategoryResponseDTO] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<CategoryEntity>): Iterable<CategoryResponseDTO>  =
        input.map(::map)
}