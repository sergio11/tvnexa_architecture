package com.dreamsoftware.data.database.entity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object SubdivisionTable: IdTable<String>(name = "categories") {
    val code = text("code")
    val country = text("country")
    val name = text("name")

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

data class SubdivisionEntity(
    val code: String,
    val country: String,
    val name: String
)