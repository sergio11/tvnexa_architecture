package com.dreamsoftware.data.database.datasource.country

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.dao.CountryEntityDAO
import com.dreamsoftware.data.database.entity.CountryEntity

interface ICountryDatabaseDataSource: ISupportDatabaseDataSource<CountryEntityDAO, String, CountryEntity>