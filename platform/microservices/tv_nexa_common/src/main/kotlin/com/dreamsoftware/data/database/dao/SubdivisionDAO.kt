package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * The `SubdivisionTable` object represents the database table for storing subdivision information.
 * It uses Exposed's `IdTable` to define the table structure.
 */
object SubdivisionTable: IdTable<String>(name = "subdivisions") {

    val code = varchar(name = "code", length = 10)
    val country = char(name = "country", length = 2).references(CountryTable.code)
    val name = varchar(name = "name", length = 100)

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

/**
 * The `SubdivisionEntityDAO` class represents the DAO (Data Access Object) for subdivision entities.
 * It extends Exposed's `Entity` class and is used for database operations related to subdivisions.
 */
class SubdivisionEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, SubdivisionEntityDAO>(SubdivisionTable)

    // Properties that map to columns in the "subdivisions" table
    var code by SubdivisionTable.code
    var name by SubdivisionTable.name

    // A reference to the associated country using the "country" column
    var country by CountryEntityDAO referencedOn SubdivisionTable.country
}