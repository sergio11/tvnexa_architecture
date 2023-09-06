package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.CategoryEntity

interface ICategoryRepository {
    suspend fun findAll(): Iterable<CategoryEntity>
    suspend fun findById(id: String): CategoryEntity
}