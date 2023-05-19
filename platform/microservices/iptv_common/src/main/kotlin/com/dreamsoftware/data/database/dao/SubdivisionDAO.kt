package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object SubdivisionTable: IdTable<String>(name = "subdivisions") {

    val code = varchar(name = "code", length = 10)
    val country = char(name = "country", length = 2).references(CountryTable.code)
    val name = varchar(name = "name", length = 100)

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

class SubdivisionEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, SubdivisionEntityDAO>(SubdivisionTable)

    var code by SubdivisionTable.code
    var name by SubdivisionTable.name
    var country by CountryEntityDAO referencedOn SubdivisionTable.country
}