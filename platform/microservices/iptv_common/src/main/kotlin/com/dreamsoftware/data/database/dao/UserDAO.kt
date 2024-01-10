package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

/**
 * UserTable: Represents the table structure for storing user-related information.
 *
 * This object defines the schema for the 'users' table in the database.
 * It includes fields like username, password, email, first name, last name,
 * creation date, and last update date.
 */
object UserTable: UUIDTable(name = "users") {

    // Unique username for each user
    val username = varchar("username", 50).uniqueIndex()

    // Password field for storing hashed passwords
    val password = varchar("password", 100)

    // Unique email for each user
    val email = varchar("email", 100).uniqueIndex()

    // First name of the user
    val firstName = varchar("first_name", 50)

    // Last name of the user
    val lastName = varchar("last_name", 50)
}

class UserEntityDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, UserEntityDAO>(UserTable)

    var username by UserTable.username
    var password by UserTable.password
    var email by UserTable.email
    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
}