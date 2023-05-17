package com.dreamsoftware.data.database.entity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object CountryTable: IdTable<String>(name = "countries") {

    val code = char(name = "code", length = 2)
    val name = varchar(name = "name", length = 20).uniqueIndex()
    val flag = char(name = "flag", length = 2).uniqueIndex()
    val languages = text("languages")

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class CountryEntity(
    val code: String,
    val name: String,
    val flag: String,
    val languages: List<String>
)