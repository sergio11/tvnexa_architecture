package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

/**
 * ProfileTable: Represents the table structure for storing user profiles.
 *
 * This object defines the schema for the 'profiles' table in the database.
 * It includes fields like user ID (foreign key), alias, type, isAdmin, pin, etc.
 */
object ProfileTable : UUIDTable(name = "profiles") {

    // Foreign key to reference the user
    val userId = uuid("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)

    // Alias for the profile
    val alias = varchar("alias", 100)

    // Type of the profile ("BOY", "GIRL", "WOMAN", "MAN")
    val type = enumerationByName("profile_type", 10, ProfileType::class)

    // Whether the profile is an admin
    val isAdmin = bool("is_admin")

    // Secret PIN for profile (6 digits), default value set to 123456
    val pin = integer("pin").default(123456)

    init {
        uniqueIndex("user_alias_unique", userId, alias)
    }
}

enum class ProfileType {
    BOY, GIRL, WOMAN, MAN
}

// Table associating profiles with blocked channels
object BlockedChannelsTable : LongIdTable(name = "blocked_channels") {

    // Reference to the profile
    val profile = reference("profile", ProfileTable)

    // Reference to the blocked channel
    val channel = reference("channel", ChannelTable)

    init {
        uniqueIndex("UNIQUE_BlockedChannels", profile, channel)
    }
}

// Table associating profiles with favorite channels
object FavoriteChannelsTable : LongIdTable(name = "favorite_channels") {

    // Reference to the profile
    val profile = reference("profile", ProfileTable)

    // Reference to the favorite channel
    val channel = reference("channel", ChannelTable)

    init {
        uniqueIndex("UNIQUE_FavoriteChannels", profile, channel)
    }
}

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

/**
 * ProfileEntity: Represents a user profile in the database.
 *
 * This class is used as a Data Access Object (DAO) for interacting with the 'profiles' table.
 */
class ProfileEntityDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, ProfileEntityDAO>(ProfileTable)

    // Foreign key to reference the user
    var userId by ProfileTable.userId

    // Alias for the profile
    var alias by ProfileTable.alias

    // Type of the profile ("BOY", "GIRL", "WOMAN", "MAN")
    var type by ProfileTable.type

    // Whether the profile is an admin
    var isAdmin by ProfileTable.isAdmin

    // Secret PIN for profile (6 digits), default value set to 123456
    var pin by ProfileTable.pin

    val blockedChannels by ChannelEntityDAO via BlockedChannelsTable
    val favoritesChannels by CategoryEntityDAO via FavoriteChannelsTable
    val favoritesCountries by CategoryEntityDAO via FavoriteCountriesTable
}