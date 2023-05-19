package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object LanguageTable: IdTable<String>(name = "languages") {

    val code = char(name = "code", length = 3)
    val name = varchar(name = "name", length = 50).uniqueIndex()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class LanguageEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, LanguageEntityDAO>(LanguageTable)

    var code by LanguageTable.code
    var name by LanguageTable.name
}