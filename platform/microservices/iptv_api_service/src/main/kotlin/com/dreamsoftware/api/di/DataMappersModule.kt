package com.dreamsoftware.api.di

import com.dreamsoftware.api.mapper.*
import com.dreamsoftware.core.getMapper
import com.dreamsoftware.core.mapper
import org.koin.dsl.module

val dataMappersModule = module {
    mapper { CategoryResponseDtoMapper() }
    mapper { ChannelGuideResponseDtoMapper(getMapper()) }
    mapper { ChannelResponseDtoMapper(getMapper(), getMapper(), getMapper(), getMapper(), getMapper()) }
    mapper { ChannelStreamResponseDtoMapper() }
    mapper { CountryResponseDtoMapper(getMapper()) }
    mapper { EpgChannelProgrammeResponseDtoMapper(getMapper(), getMapper()) }
    mapper { LanguageResponseDtoMapper() }
    mapper { RegionResponseDtoMapper(getMapper()) }
    mapper { SubdivisionResponseDtoMapper(getMapper()) }
}