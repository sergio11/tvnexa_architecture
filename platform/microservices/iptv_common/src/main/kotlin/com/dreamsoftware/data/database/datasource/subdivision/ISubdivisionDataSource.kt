package com.dreamsoftware.data.database.datasource.subdivision

import com.dreamsoftware.data.database.entity.SubdivisionEntity

interface ISubdivisionDataSource {
    suspend fun getAll(): List<SubdivisionEntity>
}