package com.dreamsoftware.data.database.datasource.country

import com.dreamsoftware.data.database.entity.CountryEntity

interface ICountryDataSource {
    suspend fun getAll(): List<CountryEntity>
}