package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.iptvorg.model.SubdivisionDTO

class SaveSubdivisionDTOMapper: IMapper<SubdivisionDTO, SaveSubdivisionEntity> {
    override fun map(input: SubdivisionDTO): SaveSubdivisionEntity = with(input) {
        SaveSubdivisionEntity(
            code = code,
            country = country,
            name = name
        )
    }
    override fun mapList(input: Iterable<SubdivisionDTO>): Iterable<SaveSubdivisionEntity> =
        input.map(::map)
}