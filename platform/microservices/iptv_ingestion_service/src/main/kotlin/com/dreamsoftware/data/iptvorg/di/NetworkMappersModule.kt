package com.dreamsoftware.data.iptvorg.di

import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.iptvorg.mapper.LanguageNetworkMapper
import org.koin.dsl.module

val networkMappers = module {
    mapper { LanguageNetworkMapper() }
}