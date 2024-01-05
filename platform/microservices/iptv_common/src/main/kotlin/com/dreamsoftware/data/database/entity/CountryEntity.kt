package com.dreamsoftware.data.database.entity

data class CountryEntity(
    val code: String,
    val name: String,
    val flag: String?,
    val languages: List<LanguageEntity>
)

data class SaveCountryEntity(
    val code: String,
    val name: String,
    val flag: String,
    val languages: List<String> = emptyList()
) {

    fun toLanguagesByCountry(): List<Pair<String, String>> =
        languages.map { Pair(code, it) }
}
