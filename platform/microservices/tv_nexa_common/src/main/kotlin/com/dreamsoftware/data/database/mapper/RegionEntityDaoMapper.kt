package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.RegionEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.RegionEntity

/**
 * Mapper implementation to convert RegionEntityDAO objects to RegionEntity objects.
 *
 * @property countryMapper The mapper used to map CountryEntityDAO objects to CountryEntity objects.
 */
class RegionEntityDaoMapper(
    private val countryMapper: ISimpleMapper<CountryEntityDAO, CountryEntity>
) : ISimpleMapper<RegionEntityDAO, RegionEntity> {

    /**
     * Maps a single RegionEntityDAO object to a RegionEntity object.
     *
     * @param input The RegionEntityDAO object to be mapped.
     * @return A mapped RegionEntity object.
     */
    override fun map(input: RegionEntityDAO): RegionEntity = with(input) {
        RegionEntity(
            code = code,
            name = name,
            countries = countryMapper.mapList(countries)
        )
    }

    /**
     * Maps a list of RegionEntityDAO objects to a list of RegionEntity objects.
     *
     * @param input An Iterable collection of RegionEntityDAO objects to be mapped.
     * @return An Iterable collection of mapped RegionEntity objects.
     */
    override fun mapList(input: Iterable<RegionEntityDAO>): Iterable<RegionEntity> =
        input.map(::map)
}