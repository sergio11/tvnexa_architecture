package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import com.dreamsoftware.data.iptvorg.model.CountryDTO

class SaveCountryDTOMapper: ISimpleMapper<CountryDTO, SaveCountryEntity> {
    override fun map(input: CountryDTO): SaveCountryEntity = with(input) {
        SaveCountryEntity(
            code = code,
            name = name,
            flag = flag,
            languages = languages
        )
    }
    override fun mapList(input: Iterable<CountryDTO>): Iterable<SaveCountryEntity> =
        input.map(::map)
}