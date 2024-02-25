package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// Table associating profiles with blocked channels
object BlockedChannelsTable : LongIdTable(name = "blocked_channels") {

    // Reference to the profile
    val profile = reference("profile", ProfileTable, onDelete = ReferenceOption.CASCADE)

    // Reference to the blocked channel
    val channel = reference("channel", ChannelTable, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex("UNIQUE_BlockedChannels", profile, channel)
    }
}

// Entity representing an entry in the blocked channels table
class BlockedChannelEntityDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<BlockedChannelEntityDAO>(BlockedChannelsTable)

    var profile by ProfileEntityDAO referencedOn BlockedChannelsTable.profile
    var channel by ChannelEntityDAO referencedOn BlockedChannelsTable.channel
}