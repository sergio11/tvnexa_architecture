package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.dao.SubdivisionEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.mapper.CategoryEntityDaoMapper
import com.dreamsoftware.data.database.mapper.CountryEntityDaoMapper
import com.dreamsoftware.data.database.mapper.LanguageEntityDaoMapper
import com.dreamsoftware.data.database.mapper.SubdivisionEntityDaoMapper
import org.koin.dsl.module

val databaseMapperModule = module {
    single<IOneSideMapper<LanguageEntityDAO, LanguageEntity>> { LanguageEntityDaoMapper() }
    single<IOneSideMapper<CategoryEntityDAO, CategoryEntity>> { CategoryEntityDaoMapper() }
    single<IOneSideMapper<CountryEntityDAO, CountryEntity>> { CountryEntityDaoMapper(get()) }
    single<IOneSideMapper<SubdivisionEntityDAO, SubdivisionEntity>> { SubdivisionEntityDaoMapper(get()) }
}