package com.dreamsoftware.data.database.datasource.language.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.dao.LanguageTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.language.ILanguageDatabaseDataSource
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class LanguageDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<LanguageEntityDAO, LanguageEntity>
): SupportDatabaseDataSource<String, LanguageEntityDAO, SaveLanguageEntity, LanguageEntity>(database, mapper, LanguageEntityDAO), ILanguageDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveLanguageEntity) = with(entityToSave) {
        this@onMapEntityToSave[LanguageTable.name] = name
        this@onMapEntityToSave[LanguageTable.code] = code
    }
}