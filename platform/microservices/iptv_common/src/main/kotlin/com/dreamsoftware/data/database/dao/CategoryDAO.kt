package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * The `CategoryTable` object represents the database table for storing category information.
 * It uses Exposed's `IdTable` to define the table structure.
 */
object CategoryTable : IdTable<String>(name = "categories") {

    val categoryId = varchar(name = "id", length = 30)
    val name = varchar(name = "name", length = 30).uniqueIndex()

    override val id: Column<EntityID<String>> = categoryId.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

/**
 * The `CategoryEntityDAO` class represents the DAO (Data Access Object) for category entities.
 * It extends Exposed's `Entity` class and is used for database operations related to categories.
 */
class CategoryEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CategoryEntityDAO>(CategoryTable)

    // Properties that map to columns in the "categories" table
    var categoryId by CategoryTable.categoryId
    var name by CategoryTable.name
}