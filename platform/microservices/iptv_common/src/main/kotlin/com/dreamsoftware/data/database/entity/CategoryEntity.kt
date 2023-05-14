package com.dreamsoftware.data.database.entity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object CategoryTable: IdTable<String>(name = "categories") {
    val categoryId = text("id")
    val name = text("name")

    override val id: Column<EntityID<String>> = categoryId.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class CategoryEntity(
    val id: String,
    val name: String
)