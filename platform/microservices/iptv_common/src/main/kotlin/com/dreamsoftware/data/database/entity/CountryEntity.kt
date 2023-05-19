package com.dreamsoftware.data.database.entity

data class CountryEntity(
    val code: String,
    val name: String,
    val flag: String,
    val languages: Iterable<LanguageEntity>
)
