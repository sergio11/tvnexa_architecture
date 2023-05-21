package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import com.dreamsoftware.data.iptvorg.model.CountryDTO
import com.dreamsoftware.data.iptvorg.model.LanguageDTO

class SaveCountryDTOMapper(
    private val languageDtoMapper: IMapper<LanguageDTO, LanguageEntity>
): IMapper<CountryDTO, SaveCountryEntity> {
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