package com.dreamsoftware.data.database.datasource.language.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.LanguageTable
import org.jetbrains.exposed.sql.ResultRow

internal class LanguageDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, LanguageEntity>
): SupportDatabaseDataSource<LanguageEntity, String>(database, mapper, LanguageTable), ILanguageDatabaseDataSource