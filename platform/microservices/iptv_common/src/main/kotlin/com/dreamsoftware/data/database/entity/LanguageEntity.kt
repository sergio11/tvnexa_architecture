package com.dreamsoftware.data.database.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object LanguageTable: IdTable<String>(name = "languages") {

    val code = text("code")
    val name = text("name")

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class LanguageEntity(id: EntityID<String>): Entity<String>(id) {

    var code by LanguageTable.code
    var name by LanguageTable.name

    companion object: EntityClass<String, LanguageEntity>(LanguageTable)
}