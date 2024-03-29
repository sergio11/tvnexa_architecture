package com.dreamsoftware.data.database.entity

data class SubdivisionEntity(
    val code: String,
    val country: CountryEntity,
    val name: String
)

data class SaveSubdivisionEntity(
    val code: String,
    val country: String,
    val name: String
)
