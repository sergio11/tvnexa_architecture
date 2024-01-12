package com.dreamsoftware.data.database.datasource.country

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SaveCountryEntity

interface ICountryDatabaseDataSource: ISupportDatabaseDataSource<String, SaveCountryEntity, CountryEntity>