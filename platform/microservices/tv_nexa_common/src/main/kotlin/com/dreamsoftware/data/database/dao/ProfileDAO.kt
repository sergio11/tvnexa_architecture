package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

/**
 * ProfileTable: Represents the table structure for storing user profiles.
 *
 * This object defines the schema for the 'profiles' table in the database.
 * It includes fields like user ID (foreign key), alias, avatar type, isAdmin, pin, etc.
 */
object ProfileTable : UUIDTable(name = "profiles") {

    // Foreign key to reference the user
    val userId = uuid("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)

    // Alias for the profile
    val alias = varchar("alias", 100)

    // Avatar type of the profile ("BOY", "GIRL", "WOMAN", "MAN")
    val avatar_type = enumerationByName("avatar_type", 10, AvatarType::class)

    // Whether the profile is an admin
    val isAdmin = bool("is_admin")

    // Secret PIN for profile (6 digits), default value set to 123456
    val pin = integer("pin").default(123456).nullable()

    // Enable NSFW for the profile
    val enableNSFW = bool("enable_nsfw").default(false)

    init {
        uniqueIndex("user_alias_unique", userId, alias)
    }
}

enum class AvatarType {
    BOY, GIRL, WOMAN, MAN
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

    // Avatar type of the profile ("BOY", "GIRL", "WOMAN", "MAN")
    var avatarType by ProfileTable.avatar_type

    // Whether the profile is an admin
    var isAdmin by ProfileTable.isAdmin

    // Secret PIN for profile (6 digits), default value set to 123456
    var pin by ProfileTable.pin

    // Enable NSFW for the profile
    var enableNSFW by ProfileTable.enableNSFW
}