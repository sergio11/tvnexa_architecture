package com.dreamsoftware.data.database.entity

data class RegionEntity(
    val code: String,
    val name: String,
    val countries: Iterable<CountryEntity>
)
