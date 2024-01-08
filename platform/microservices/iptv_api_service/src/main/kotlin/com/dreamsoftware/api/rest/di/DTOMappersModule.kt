package com.dreamsoftware.api.rest.di

import com.dreamsoftware.api.rest.mapper.*
import com.dreamsoftware.core.getMapper
import com.dreamsoftware.core.mapper
import org.koin.dsl.module

val dtoMappersModule = module {
    mapper { CategoryResponseDtoMapper() }
    mapper { ChannelGuideResponseDtoMapper() }
    mapper { ChannelDetailResponseDtoMapper(getMapper(), getMapper(), getMapper(), getMapper(), getMapper(), getMapper()) }
    mapper { SimpleChannelResponseDtoMapper() }
    mapper { ChannelStreamResponseDtoMapper() }
    mapper { CountryResponseDtoMapper(getMapper()) }
    mapper { EpgChannelProgrammeResponseDtoMapper(getMapper(), getMapper()) }
    mapper { LanguageResponseDtoMapper() }
    mapper { RegionResponseDtoMapper(getMapper()) }
    mapper { SubdivisionResponseDtoMapper(getMapper()) }
}