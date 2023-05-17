package com.dreamsoftware.data.database.entity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object LanguageTable: IdTable<String>(name = "languages") {

    val code = char(name = "code", length = 3)
    val name = varchar(name = "name", length = 50).uniqueIndex()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class LanguageEntity(
    val code: String,
    val name: String
)