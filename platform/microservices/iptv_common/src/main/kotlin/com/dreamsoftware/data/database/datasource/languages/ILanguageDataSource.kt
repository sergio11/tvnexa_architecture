package com.dreamsoftware.data.database.datasource.languages

import com.dreamsoftware.data.database.entity.LanguageEntity

interface ILanguageDataSource {
    fun getAll(): List<LanguageEntity>
}