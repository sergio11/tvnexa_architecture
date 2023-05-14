package com.dreamsoftware.data.database.di

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.DatabaseFactoryImpl
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.category.ICategoryDataSource
import com.dreamsoftware.data.database.datasource.category.impl.CategoryDataSourceImpl
import com.dreamsoftware.data.database.datasource.country.ICountryDataSource
import com.dreamsoftware.data.database.datasource.country.impl.CountryDataSourceImpl
import com.dreamsoftware.data.database.datasource.language.ILanguageDataSource
import com.dreamsoftware.data.database.datasource.language.impl.LanguageDataSourceImpl
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDataSource
import com.dreamsoftware.data.database.datasource.subdivision.impl.SubdivisionDataSourceImpl
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.mapper.CategoryTableMapper
import com.dreamsoftware.data.database.mapper.CountryTableMapper
import com.dreamsoftware.data.database.mapper.LanguageTableMapper
import com.dreamsoftware.data.database.mapper.SubdivisionTableMapper
import com.dreamsoftware.model.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.ResultRow
import org.koin.dsl.module
import javax.sql.DataSource

val databaseModule = module {
    single<DataSource> {
        with(get<DatabaseConfig>()) {
            HikariDataSource(HikariConfig().apply {
                driverClassName = driverClass
                jdbcUrl = databaseUrl
                username = connUser
                password = connPassword
                maximumPoolSize = maxPoolSize
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            })
        }
    }
    single<IDatabaseFactory> { DatabaseFactoryImpl(get()) }
    factory<IOneSideMapper<ResultRow, LanguageEntity>> { LanguageTableMapper() }
    factory<IOneSideMapper<ResultRow, CountryEntity>> { CountryTableMapper() }
    factory<IOneSideMapper<ResultRow, CategoryEntity>> { CategoryTableMapper() }
    factory<IOneSideMapper<ResultRow, SubdivisionEntity>> { SubdivisionTableMapper() }
    factory<ILanguageDataSource> { LanguageDataSourceImpl(get(), get()) }
    factory<ICountryDataSource> { CountryDataSourceImpl(get(), get()) }
    factory<ICategoryDataSource> { CategoryDataSourceImpl(get(), get()) }
    factory<ISubdivisionDataSource> { SubdivisionDataSourceImpl(get(), get()) }
}