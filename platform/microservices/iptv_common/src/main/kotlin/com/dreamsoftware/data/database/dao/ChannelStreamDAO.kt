package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object ChannelStreamTable: IdTable<String>(name = "channel_streams") {
    // Channel Stream Code Hash
    val code = varchar(name = "code", length = 64).uniqueIndex()
    // Channel ID
    val channel = varchar(name = "channel", length = 50).references(ChannelTable.channelId)
    // Stream URL
    val url = varchar(name = "url", length = 1000)
    // The Referer request header for the stream
    val httpReferrer = varchar(name = "http_referrer", length = 500).nullable()
    // The User-Agent request header for the stream
    val userAgent = varchar(name = "user_agent", length = 500).nullable()

    override val id: Column<EntityID<String>> = code.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(code)
}

class ChannelStreamEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ChannelStreamEntityDAO>(ChannelStreamTable)

    var code by ChannelStreamTable.code
    var channel by ChannelEntityDAO referencedOn ChannelStreamTable.channel
    var url by ChannelStreamTable.url
    var httpReferrer by ChannelStreamTable.httpReferrer
    var userAgent by ChannelStreamTable.userAgent
}