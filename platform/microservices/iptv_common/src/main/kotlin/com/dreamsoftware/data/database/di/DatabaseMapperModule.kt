package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.mapper.CategoryTableMapper
import com.dreamsoftware.data.database.mapper.CountryTableMapper
import com.dreamsoftware.data.database.mapper.LanguageTableMapper
import com.dreamsoftware.data.database.mapper.SubdivisionTableMapper
import org.jetbrains.exposed.sql.ResultRow
import org.koin.dsl.module

val databaseMapperModule = module {
    factory<IOneSideMapper<ResultRow, LanguageEntity>> { LanguageTableMapper() }
    factory<IOneSideMapper<ResultRow, CountryEntity>> { CountryTableMapper() }
    factory<IOneSideMapper<ResultRow, CategoryEntity>> { CategoryTableMapper() }
    factory<IOneSideMapper<ResultRow, SubdivisionEntity>> { SubdivisionTableMapper() }
}