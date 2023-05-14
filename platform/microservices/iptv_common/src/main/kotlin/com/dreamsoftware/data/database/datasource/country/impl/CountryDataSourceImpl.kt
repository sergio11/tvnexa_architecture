package com.dreamsoftware.data.database.datasource.country.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.country.ICountryDataSource
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.CountryTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

internal class CountryDataSourceImpl(
    private val database: IDatabaseFactory,
    private val mapper: IOneSideMapper<ResultRow, CountryEntity>
): ICountryDataSource {

    override suspend fun getAll(): List<CountryEntity> = withContext(Dispatchers.IO) {
        database.dbExec {
            CountryTable.selectAll().map(mapper::map)
        }
    }
}