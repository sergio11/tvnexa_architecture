package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

/**
 * The `RegionTable` object represents the database table for storing region information.
 * It uses Exposed's `IdTable` to define the table structure.
 */
object RegionTable: IdTable<String>(name = "regions") {

    val code = varchar(name = "code", length = 10)
    val name = varchar(name = "name", length = 100).uniqueIndex()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

/**
 * The `RegionCountryTable` object represents the database table for associating regions with countries.
 * It uses Exposed's `LongIdTable` to define the table structure.
 */
object RegionCountryTable: LongIdTable(name = "regions_countries") {

    val region = reference("region", RegionTable)
    val country = reference("country", CountryTable)

    init {
        uniqueIndex("UNIQUE_RegionCountry", region, country)
    }
}

/**
 * The `RegionEntityDAO` class represents the DAO (Data Access Object) for region entities.
 * It extends Exposed's `Entity` class and is used for database operations related to regions.
 */
class RegionEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, RegionEntityDAO>(RegionTable)

    // Properties that map to columns in the "regions" table
    var code by CountryTable.code
    var name by CountryTable.name

    // A reference to associated countries using the "regions_countries" table
    val countries by CountryEntityDAO referrersOn RegionCountryTable.region
}