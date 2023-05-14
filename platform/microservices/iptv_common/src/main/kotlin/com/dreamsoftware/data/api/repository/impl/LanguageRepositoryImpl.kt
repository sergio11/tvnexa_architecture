package com.dreamsoftware.data.api.repository.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.api.repository.ILanguageRepository
import com.dreamsoftware.data.database.datasource.language.ILanguageDataSource
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.model.Language

internal class LanguageRepositoryImpl(
    private val languageDataSource: ILanguageDataSource,
    private val languageMapper: IMapper<LanguageEntity, Language>
): ILanguageRepository {
}