package com.dreamsoftware.data.database.datasource.category

import com.dreamsoftware.data.database.entity.CategoryEntity

interface ICategoryDataSource {
    suspend fun getAll(): List<CategoryEntity>
}