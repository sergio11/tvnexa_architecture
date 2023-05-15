package com.dreamsoftware.data.database.di

import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.category.impl.CategoryDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.impl.CountryDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.datasource.language.impl.LanguageDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.datasource.subdivision.impl.SubdivisionDatabaseDataSourceImpl
import org.koin.dsl.module

val databaseDataSourcesModule = module {
    includes(databaseMapperModule, databaseModule)
    factory<ILanguageDatabaseDataSource> { LanguageDatabaseDataSourceImpl(get(), get()) }
    factory<ICountryDatabaseDataSource> { CountryDatabaseDataSourceImpl(get(), get()) }
    factory<ICategoryDatabaseDataSource> { CategoryDatabaseDataSourceImpl(get(), get()) }
    factory<ISubdivisionDatabaseDataSource> { SubdivisionDatabaseDataSourceImpl(get(), get()) }
}