package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object RegionTable: IdTable<String>(name = "regions") {

    val code = varchar(name = "code", length = 10)
    val name = varchar(name = "name", length = 100).uniqueIndex()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object RegionCountryTable: LongIdTable(name = "regions_countries") {

    val region = reference("region", RegionTable)
    val country = reference("country", CountryTable)

    init {
        uniqueIndex("UNIQUE_RegionCountry", region, country)
    }
}

class RegionEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, RegionEntityDAO>(RegionTable)

    var code by CountryTable.code
    var name by CountryTable.name
    val countries by CountryEntityDAO referrersOn RegionCountryTable.region
}