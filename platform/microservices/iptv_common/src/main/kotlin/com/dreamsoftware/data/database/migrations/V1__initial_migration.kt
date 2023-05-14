package com.dreamsoftware.data.database.migrations

import com.dreamsoftware.data.database.entity.CategoryTable
import com.dreamsoftware.data.database.entity.CountryTable
import com.dreamsoftware.data.database.entity.LanguageTable
import com.dreamsoftware.data.database.entity.SubdivisionTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1__initial_migration: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(LanguageTable)
            SchemaUtils.create(CountryTable)
            SchemaUtils.create(CategoryTable)
            SchemaUtils.create(SubdivisionTable)
        }
    }
}