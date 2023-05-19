package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object CategoryTable : IdTable<String>(name = "categories") {

    val categoryId = varchar(name = "id", length = 30)
    val name = varchar(name = "name", length = 30).uniqueIndex()

    override val id: Column<EntityID<String>> = categoryId.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class CategoryEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CategoryEntityDAO>(CategoryTable)

    var categoryId by CategoryTable.categoryId
    var name by CategoryTable.name
}