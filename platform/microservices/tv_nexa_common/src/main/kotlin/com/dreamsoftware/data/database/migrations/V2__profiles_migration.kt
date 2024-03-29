package com.dreamsoftware.data.database.migrations

import com.dreamsoftware.data.database.dao.BlockedChannelsTable
import com.dreamsoftware.data.database.dao.FavoriteChannelsTable
import com.dreamsoftware.data.database.dao.FavoriteCountriesTable
import com.dreamsoftware.data.database.dao.ProfileTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V2__profiles_migration: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(ProfileTable)
            SchemaUtils.create(FavoriteChannelsTable)
            SchemaUtils.create(BlockedChannelsTable)
            SchemaUtils.create(FavoriteCountriesTable)
        }
    }
}