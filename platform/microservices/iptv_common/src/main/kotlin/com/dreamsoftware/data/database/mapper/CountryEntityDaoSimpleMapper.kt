package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity

class CountryEntityDaoSimpleMapper(
    private val languageMapper: ISimpleMapper<LanguageEntityDAO, LanguageEntity>
): ISimpleMapper<CountryEntityDAO, CountryEntity> {

    override fun map(input: CountryEntityDAO): CountryEntity = with(input) {
        CountryEntity(
            code = code,
            name = name.orEmpty(),
            flag = flag,
            languages = languageMapper.mapList(languages)
        )
    }

    override fun mapList(input: Iterable<CountryEntityDAO>): Iterable<CountryEntity> =
        input.map(::map)
}