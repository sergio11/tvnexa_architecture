package com.dreamsoftware.data.database.datasource.language

import com.dreamsoftware.data.database.entity.LanguageEntity

interface ILanguageDataSource {
    suspend fun getAll(): List<LanguageEntity>
}