package com.dreamsoftware.data.database.datasource.language

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.entity.LanguageEntity

interface ILanguageDatabaseDataSource: ISupportDatabaseDataSource<LanguageEntityDAO, String, LanguageEntity>