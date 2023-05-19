package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object CountryTable: IdTable<String>(name = "countries") {

    val code = char(name = "code", length = 2)
    val name = varchar(name = "name", length = 20).uniqueIndex()
    val flag = char(name = "flag", length = 2).uniqueIndex()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

object CountryLanguageTable : Table(name = "countries_languages") {

    val country = reference("country", CountryTable)
    val language = reference("language", LanguageTable)

    override val primaryKey = PrimaryKey(country, language, name = "PK_CountryLanguage")
}

class CountryEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CountryEntityDAO>(CountryTable)

    var code by CountryTable.code
    var name by CountryTable.name
    var flag by CountryTable.flag
    val languages by LanguageEntityDAO referrersOn CountryLanguageTable.country
}