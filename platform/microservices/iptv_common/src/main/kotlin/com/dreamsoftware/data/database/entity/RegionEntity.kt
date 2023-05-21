package com.dreamsoftware.data.database.entity

data class RegionEntity(
    val code: String,
    val name: String,
    val countries: Iterable<CountryEntity>
)

data class SaveRegionEntity(
    val code: String,
    val name: String,
    val countries: Iterable<String>
) {

    fun toCountriesByRegion(): Iterable<Pair<String, String>> =
        countries.map { Pair(code, it) }
}
