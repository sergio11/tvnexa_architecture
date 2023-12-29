package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.getMapper
import com.dreamsoftware.core.mapper
import com.dreamsoftware.data.database.mapper.*
import org.koin.dsl.module

val databaseMapperModule = module {
    mapper { LanguageEntityDaoSimpleMapper() }
    mapper { CategoryEntityDaoSimpleMapper() }
    mapper { CountryEntityDaoSimpleMapper(getMapper()) }
    mapper { SubdivisionEntityDaoSimpleMapper(getMapper()) }
    mapper { RegionEntityDaoSimpleMapper(getMapper()) }
    mapper { ChannelEntityDaoSimpleMapper(getMapper(), getMapper(), getMapper(), getMapper(), getMapper()) }
    mapper { ChannelStreamEntityDaoSimpleMapper() }
    mapper { ChannelGuideEntityDaoSimpleMapper() }
    mapper { EpgChannelProgrammeDaoSimpleMapper(getMapper(), getMapper()) }
    mapper { CatchupEntityDaoSimpleMapper() }
}