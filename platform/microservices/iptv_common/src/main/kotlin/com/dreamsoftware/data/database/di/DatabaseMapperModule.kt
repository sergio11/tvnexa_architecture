package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*
import com.dreamsoftware.data.database.mapper.*
import org.koin.dsl.module

val databaseMapperModule = module {
    single<IOneSideMapper<LanguageEntityDAO, LanguageEntity>> { LanguageEntityDaoMapper() }
    single<IOneSideMapper<CategoryEntityDAO, CategoryEntity>> { CategoryEntityDaoMapper() }
    single<IOneSideMapper<CountryEntityDAO, CountryEntity>> { CountryEntityDaoMapper(get()) }
    single<IOneSideMapper<SubdivisionEntityDAO, SubdivisionEntity>> { SubdivisionEntityDaoMapper(get()) }
    single<IOneSideMapper<RegionEntityDAO, RegionEntity>> { RegionEntityDaoMapper(get()) }
    single<IOneSideMapper<ChannelEntityDAO, ChannelEntity>> { ChannelEntityDaoMapper(get(), get(), get(), get()) }
}