package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.SubdivisionEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity

class SubdivisionEntityDaoMapper(
    private val countryMapper: IOneSideMapper<CountryEntityDAO, CountryEntity>
): IOneSideMapper<SubdivisionEntityDAO, SubdivisionEntity> {

    override fun map(input: SubdivisionEntityDAO): SubdivisionEntity = with(input) {
        SubdivisionEntity(
            code = code,
            name = name,
            country = countryMapper.map(country)
        )
    }

    override fun mapList(input: Iterable<SubdivisionEntityDAO>): Iterable<SubdivisionEntity> =
        input.map(::map)
}