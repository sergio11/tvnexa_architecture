package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * The `LanguageTable` object represents the database table for storing language information.
 * It uses Exposed's `IdTable` to define the table structure.
 */
object LanguageTable: IdTable<String>(name = "languages") {

    val code = char(name = "code", length = 3)
    val name = varchar(name = "name", length = 100).uniqueIndex()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

/**
 * The `LanguageEntityDAO` class represents the DAO (Data Access Object) for language entities.
 * It extends Exposed's `Entity` class and is used for database operations related to languages.
 */
class LanguageEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, LanguageEntityDAO>(LanguageTable)

    // Properties that map to columns in the "languages" table
    var code by LanguageTable.code
    var name by LanguageTable.name
}