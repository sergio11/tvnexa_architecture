package com.dreamsoftware.data.iptvorg.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.model.Language

class LanguageNetworkMapper: IMapper<LanguageDTO, Language> {
    override fun map(input: LanguageDTO): Language = with(input) {
        Language(code, name)
    }

    override fun mapList(input: Iterable<LanguageDTO>): Iterable<Language> =
        input.map(::map)
}
