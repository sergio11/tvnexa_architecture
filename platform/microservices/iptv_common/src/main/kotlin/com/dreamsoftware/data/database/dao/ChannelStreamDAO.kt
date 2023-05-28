package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ChannelStreamTable: LongIdTable(name = "channel_streams") {

    // Channel ID
    val channelId = varchar(name = "channel", length = 50).references(ChannelTable.channelId)
    // Stream URL
    val url = varchar(name = "url", length = 1000)
    // The Referer request header for the stream
    val httpReferrer = varchar(name = "http_referrer", length = 500).nullable()
    // The User-Agent request header for the stream
    val userAgent = varchar(name = "user_agent", length = 500).nullable()

}

class ChannelStreamEntityDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, ChannelStreamEntityDAO>(ChannelStreamTable)

    var channel by ChannelEntityDAO referencedOn ChannelStreamTable.channelId
    var url by ChannelStreamTable.url
    var httpReferrer by ChannelStreamTable.httpReferrer
    var userAgent by ChannelStreamTable.userAgent
}