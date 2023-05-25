package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.getMapper
import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.database.mapper.*
import org.koin.dsl.module

val databaseMapperModule = module {
    mapper { LanguageEntityDaoMapper() }
    mapper { CategoryEntityDaoMapper() }
    mapper { CountryEntityDaoMapper(getMapper()) }
    mapper { SubdivisionEntityDaoMapper(getMapper()) }
    mapper { RegionEntityDaoMapper(getMapper()) }
    mapper { ChannelEntityDaoMapper(getMapper(), getMapper(), getMapper(), getMapper()) }
    mapper { ChannelStreamEntityDaoMapper(getMapper()) }
    mapper { ChannelGuideEntityDaoMapper(getMapper()) }
}