package com.dreamsoftware.data.database.datasource.language

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.SaveLanguageEntity

interface ILanguageDatabaseDataSource: ISupportDatabaseDataSource<String, SaveLanguageEntity, LanguageEntity>