package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

// Table associating profiles with favorite countries
object FavoriteCountriesTable : LongIdTable(name = "favorite_countries") {

    // Reference to the profile
    val profile = reference("profile", ProfileTable)

    // Reference to the favorite country
    val country = reference("country", CountryTable)

    init {
        uniqueIndex("UNIQUE_FavoriteCountries", profile, country)
    }
}

// Entity representing an entry in the favorite countries table
class FavoriteCountryEntityDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FavoriteCountryEntityDAO>(FavoriteCountriesTable)

    var profile by ProfileEntityDAO referencedOn FavoriteCountriesTable.profile
    var country by CountryEntityDAO referencedOn FavoriteCountriesTable.country
}