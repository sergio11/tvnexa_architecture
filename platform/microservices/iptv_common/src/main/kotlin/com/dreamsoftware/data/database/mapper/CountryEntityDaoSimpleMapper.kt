package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity

/**
 * Mapper for converting [CountryEntityDAO] objects to [CountryEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([CountryEntityDAO])
 * to the domain representation ([CountryEntity]) of a country entity.
 *
 * @param languageMapper A mapper for converting [LanguageEntityDAO] to [LanguageEntity].
 */
class CountryEntityDaoSimpleMapper(
    private val languageMapper: ISimpleMapper<LanguageEntityDAO, LanguageEntity>
) : ISimpleMapper<CountryEntityDAO, CountryEntity> {

    /**
     * Maps a single [CountryEntityDAO] object to a [CountryEntity] object.
     *
     * @param input The input [CountryEntityDAO] to map.
     * @return The mapped [CountryEntity].
     */
    override fun map(input: CountryEntityDAO): CountryEntity = with(input) {
        CountryEntity(
            code = code,
            name = name.orEmpty(),
            flag = flag,
            languages = languageMapper.mapList(languages)
        )
    }

    /**
     * Maps a list of [CountryEntityDAO] objects to a list of [CountryEntity] objects.
     *
     * @param input The input list of [CountryEntityDAO] objects to map.
     * @return The mapped list of [CountryEntity] objects.
     */
    override fun mapList(input: Iterable<CountryEntityDAO>): Iterable<CountryEntity> =
        input.map(::map)
}