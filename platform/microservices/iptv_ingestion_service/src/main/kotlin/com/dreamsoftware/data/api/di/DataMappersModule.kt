package com.dreamsoftware.data.api.di

import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.api.mapper.*
import org.koin.dsl.module

val dataMappersModule = module {
    mapper { SaveLanguageDTOMapper() }
    mapper { SaveCategoryDTOMapper() }
    mapper { SaveCountryDTOMapper(get()) }
    mapper { SaveSubdivisionDTOMapper() }
    mapper { SaveRegionDTOMapper() }
    mapper { SaveChannelDTOMapper() }
    mapper { SaveStreamDTOMapper() }
}