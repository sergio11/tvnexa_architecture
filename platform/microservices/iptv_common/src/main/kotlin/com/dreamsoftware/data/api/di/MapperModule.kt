package com.dreamsoftware.data.api.di

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.api.mapper.LanguageMapper
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.model.Language
import org.koin.dsl.module

val mapperModule = module {
    factory<IMapper<LanguageEntity, Language>> { LanguageMapper() }
}