package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.SaveRegionEntity
import com.dreamsoftware.data.iptvorg.model.RegionDTO

class SaveRegionDTOMapper: IMapper<RegionDTO, SaveRegionEntity> {
    override fun map(input: RegionDTO): SaveRegionEntity = with(input) {
        SaveRegionEntity(
            code = code,
            name = name,
            countries = countries
        )
    }
    override fun mapList(input: Iterable<RegionDTO>): Iterable<SaveRegionEntity> =
        input.map(::map)
}