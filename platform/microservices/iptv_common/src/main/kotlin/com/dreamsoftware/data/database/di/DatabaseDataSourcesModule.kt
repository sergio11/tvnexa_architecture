package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.getMapper
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
    single<ILanguageDatabaseDataSource> { LanguageDatabaseDataSourceImpl(get(), getMapper()) }
    single<ICountryDatabaseDataSource> { CountryDatabaseDataSourceImpl(get(), getMapper()) }
    single<ICategoryDatabaseDataSource> { CategoryDatabaseDataSourceImpl(get(), getMapper()) }
    single<ISubdivisionDatabaseDataSource> { SubdivisionDatabaseDataSourceImpl(get(), getMapper()) }
    single<IRegionDatabaseDataSource> { RegionDatabaseDataSourceImpl(get(), getMapper()) }
    single<IChannelDatabaseDataSource> { ChannelDatabaseDataSourceImpl(get(), getMapper())  }
}