package com.dreamsoftware.data.database.migrations

import com.dreamsoftware.data.database.dao.*
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1__initial_migration: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(LanguageTable)
            SchemaUtils.create(CountryTable)
            SchemaUtils.create(CountryLanguageTable)
            SchemaUtils.create(CategoryTable)
            SchemaUtils.create(SubdivisionTable)
        }
    }
}