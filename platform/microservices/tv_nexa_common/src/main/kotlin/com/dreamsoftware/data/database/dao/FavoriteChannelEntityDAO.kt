package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

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

// Entity representing an entry in the favorite channels table
class FavoriteChannelEntityDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FavoriteChannelEntityDAO>(FavoriteChannelsTable)

    var profile by ProfileEntityDAO referencedOn FavoriteChannelsTable.profile
    var channel by ChannelEntityDAO referencedOn FavoriteChannelsTable.channel
}