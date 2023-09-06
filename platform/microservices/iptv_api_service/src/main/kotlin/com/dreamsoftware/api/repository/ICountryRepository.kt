package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.CountryEntity

interface ICountryRepository {
    suspend fun findAll(): Iterable<CountryEntity>
    suspend fun findByCode(code: String): CountryEntity
}