package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import com.dreamsoftware.data.iptvorg.model.CategoryDTO

class SaveCategoryDTOMapper: ISimpleMapper<CategoryDTO, SaveCategoryEntity> {
    override fun map(input: CategoryDTO): SaveCategoryEntity = with(input) {
        SaveCategoryEntity(id, name)
    }
    override fun mapList(input: Iterable<CategoryDTO>): Iterable<SaveCategoryEntity> =
        input.map(::map)
}