package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.RegionEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.RegionEntity

class RegionEntityDaoSimpleMapper(
    private val countryMapper: ISimpleMapper<CountryEntityDAO, CountryEntity>
): ISimpleMapper<RegionEntityDAO, RegionEntity> {

    override fun map(input: RegionEntityDAO): RegionEntity = with(input) {
        RegionEntity(
            code = code,
            name = name.orEmpty(),
            countries = countryMapper.mapList(countries)
        )
    }

    override fun mapList(input: Iterable<RegionEntityDAO>): Iterable<RegionEntity> =
        input.map(::map)
}