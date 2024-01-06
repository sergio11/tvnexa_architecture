package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.SubdivisionEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity

/**
 * Mapper implementation to convert SubdivisionEntityDAO objects to SubdivisionEntity objects.
 *
 * @property countryMapper The mapper used to map CountryEntityDAO objects to CountryEntity objects.
 */
class SubdivisionEntityDaoMapper(
    private val countryMapper: ISimpleMapper<CountryEntityDAO, CountryEntity>
) : ISimpleMapper<SubdivisionEntityDAO, SubdivisionEntity> {

    /**
     * Maps a single SubdivisionEntityDAO object to a SubdivisionEntity object.
     *
     * @param input The SubdivisionEntityDAO object to be mapped.
     * @return A mapped SubdivisionEntity object.
     */
    override fun map(input: SubdivisionEntityDAO): SubdivisionEntity = with(input) {
        SubdivisionEntity(
            code = code,
            name = name,
            country = countryMapper.map(country)
        )
    }

    /**
     * Maps a list of SubdivisionEntityDAO objects to a list of SubdivisionEntity objects.
     *
     * @param input An Iterable collection of SubdivisionEntityDAO objects to be mapped.
     * @return An Iterable collection of mapped SubdivisionEntity objects.
     */
    override fun mapList(input: Iterable<SubdivisionEntityDAO>): Iterable<SubdivisionEntity> =
        input.map(::map)
}