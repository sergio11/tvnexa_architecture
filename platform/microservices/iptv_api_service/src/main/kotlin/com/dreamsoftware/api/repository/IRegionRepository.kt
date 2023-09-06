package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.RegionEntity

interface IRegionRepository {
    suspend fun findAll(): Iterable<RegionEntity>
    suspend fun findByCode(code: String): RegionEntity
}