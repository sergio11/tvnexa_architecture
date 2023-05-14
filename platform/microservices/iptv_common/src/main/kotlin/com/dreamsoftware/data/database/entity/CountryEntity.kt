package com.dreamsoftware.data.database.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object CountryTable: IntIdTable(name = "countries") {
    val name = text("name")
    val code = text("code")
    val flag = text("flag")
    val languages = text("languages")
}

data class CountryEntity(
    val id: Int,
    val name: String,
    val code: String,
    val flag: String,
    val languages: List<String>
)