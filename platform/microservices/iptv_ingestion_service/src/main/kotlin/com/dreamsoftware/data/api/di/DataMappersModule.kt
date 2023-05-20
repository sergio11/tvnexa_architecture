package com.dreamsoftware.data.api.di

import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.api.mapper.LanguageDTOMapper
import org.koin.dsl.module

val dataMappersModule = module {
    mapper { LanguageDTOMapper() }
}