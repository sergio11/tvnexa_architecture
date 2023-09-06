package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.api.repository.IRegionRepository
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity

class RegionRepositoryImpl(
    private val regionDatabaseDataSource: IRegionDatabaseDataSource
): IRegionRepository {

    override suspend fun findAll(): Iterable<RegionEntity> =
        regionDatabaseDataSource.findAll()

    override suspend fun findByCode(code: String): RegionEntity =
        regionDatabaseDataSource.findByKey(code)
}