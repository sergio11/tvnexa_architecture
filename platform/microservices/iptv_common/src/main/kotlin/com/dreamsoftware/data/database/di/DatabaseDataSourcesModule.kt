package com.dreamsoftware.data.database.di

import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.category.impl.CategoryDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.channel.impl.ChannelDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.country.impl.CountryDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.datasource.language.impl.LanguageDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.datasource.region.impl.RegionDatabaseDataSourceImpl
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.datasource.subdivision.impl.SubdivisionDatabaseDataSourceImpl
import org.koin.dsl.module

val databaseDataSourcesModule = module {
    includes(databaseMapperModule, databaseModule)
    single<ILanguageDatabaseDataSource> { LanguageDatabaseDataSourceImpl(get(), get()) }
    single<ICountryDatabaseDataSource> { CountryDatabaseDataSourceImpl(get(), get()) }
    single<ICategoryDatabaseDataSource> { CategoryDatabaseDataSourceImpl(get(), get()) }
    single<ISubdivisionDatabaseDataSource> { SubdivisionDatabaseDataSourceImpl(get(), get()) }
    single<IRegionDatabaseDataSource> { RegionDatabaseDataSourceImpl(get(), get()) }
    single<IChannelDatabaseDataSource> { ChannelDatabaseDataSourceImpl(get(), get())  }
}