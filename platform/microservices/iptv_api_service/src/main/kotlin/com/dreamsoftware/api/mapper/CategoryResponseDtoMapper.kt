package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.CategoryResponseDTO
import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.CategoryEntity

class CategoryResponseDtoMapper : IMapper<CategoryEntity, CategoryResponseDTO> {
    override fun map(input: CategoryEntity): CategoryResponseDTO =
        CategoryResponseDTO(
            id = input.id,
            name = input.name
        )

    override fun mapList(input: Iterable<CategoryEntity>): Iterable<CategoryResponseDTO>  =
        input.map(::map)
}