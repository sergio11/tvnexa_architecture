package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object CountryTable: IdTable<String>(name = "countries") {

    val code = char(name = "code", length = 2)
    val name = varchar(name = "name", length = 100).uniqueIndex()
    val flag = char(name = "flag", length = 10)

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object CountryLanguageTable : LongIdTable(name = "countries_languages") {

    val country = reference("country", CountryTable)
    val language = reference("language", LanguageTable)

    init {
        uniqueIndex("UNIQUE_CountryLanguage", country, language)
    }

}

class CountryEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CountryEntityDAO>(CountryTable)

    var code by CountryTable.code
    var name by CountryTable.name
    var flag by CountryTable.flag
    val languages by LanguageEntityDAO referrersOn CountryLanguageTable.country
}