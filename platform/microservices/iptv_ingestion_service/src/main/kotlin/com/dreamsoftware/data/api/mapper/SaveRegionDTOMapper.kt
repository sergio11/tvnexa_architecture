package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveRegionEntity
import com.dreamsoftware.data.iptvorg.model.RegionDTO

class SaveRegionDTOMapper: ISimpleMapper<RegionDTO, SaveRegionEntity> {
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