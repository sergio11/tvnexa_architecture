package com.dreamsoftware.data.database.entity

data class CountryEntity(
    val code: String,
    val name: String,
    val flag: String?,
    val languages: Iterable<LanguageEntity>
)

data class SaveCountryEntity(
    val code: String,
    val name: String,
    val flag: String,
    val languages: List<String> = emptyList()
) {

    fun toLanguagesByCountry(): Iterable<Pair<String, String>> =
        languages.map { Pair(code, it) }
}
