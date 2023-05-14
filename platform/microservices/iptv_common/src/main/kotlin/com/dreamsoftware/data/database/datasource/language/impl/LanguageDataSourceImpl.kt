package com.dreamsoftware.data.database.datasource.language.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.language.ILanguageDataSource
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.LanguageTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class LanguageDataSourceImpl(
    private val database: IDatabaseFactory,
    private val mapper: IOneSideMapper<ResultRow, LanguageEntity>
): ILanguageDataSource {

    override suspend fun getAll(): List<LanguageEntity> = withContext(Dispatchers.IO) {
        database.dbExec { LanguageTable.selectAll().map(mapper::map) }
    }
}