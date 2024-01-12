package com.dreamsoftware.data.api.di

import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.api.mapper.*
import org.koin.dsl.module

val dataMappersModule = module {
    // Define mappers for different DTOs
    mapper { SaveLanguageDTOMapper() }
    mapper { SaveCategoryDTOMapper() }
    mapper { SaveCountryDTOMapper() }
    mapper { SaveSubdivisionDTOMapper() }
    mapper { SaveRegionDTOMapper() }
    mapper { SaveChannelDTOMapper() }
    mapper { SaveChannelStreamDTOMapper() }
    mapper { SaveChannelGuideDTOMapper() }
    mapper { SaveEpgDataDTOMapper() }
}