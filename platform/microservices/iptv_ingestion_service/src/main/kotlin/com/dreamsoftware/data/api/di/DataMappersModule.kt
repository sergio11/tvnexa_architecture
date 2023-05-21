package com.dreamsoftware.data.api.di

import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.api.mapper.SaveCategoryDTOMapper
import com.dreamsoftware.data.api.mapper.SaveCountryDTOMapper
import com.dreamsoftware.data.api.mapper.SaveLanguageDTOMapper
import org.koin.dsl.module

val dataMappersModule = module {
    mapper { SaveLanguageDTOMapper() }
    mapper { SaveCategoryDTOMapper() }
    mapper { SaveCountryDTOMapper(get()) }
}