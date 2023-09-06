package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.data.database.datasource.country.ICountryDatabaseDataSource
import com.dreamsoftware.api.repository.ICountryRepository
import com.dreamsoftware.data.database.entity.CountryEntity

class CountryRepositoryImpl(
    private val countryDatabaseDataSource: ICountryDatabaseDataSource
): ICountryRepository {

    override suspend fun findAll(): Iterable<CountryEntity> =
        countryDatabaseDataSource.findAll()

    override suspend fun findByCode(code: String): CountryEntity =
        countryDatabaseDataSource.findByKey(code)
}