package com.dreamsoftware.data.database.datasource.languages

import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.LanguageTable
import org.jetbrains.exposed.sql.selectAll

class LanguageDataSourceImpl: ILanguageDataSource {

    override fun getAll(): List<LanguageEntity> =
        LanguageEntity.wrapRows(LanguageTable.selectAll()).toList()
}