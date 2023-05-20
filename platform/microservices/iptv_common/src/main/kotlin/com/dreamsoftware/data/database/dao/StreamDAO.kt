package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object StreamTable: LongIdTable(name = "streams") {

    val channelId = varchar(name = "channel", length = 20).references(ChannelTable.channelId)
    val url = varchar(name = "url", length = 300)
    val httpReferrer = varchar(name = "http_referrer", length = 300).nullable()
    val userAgent = varchar(name = "user_agent", length = 100).nullable()

}

class StreamEntityDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, StreamEntityDAO>(StreamTable)

    var channel by ChannelEntityDAO referencedOn StreamTable.channelId
    var url by StreamTable.url
    var httpReferrer by StreamTable.httpReferrer
    var userAgent by StreamTable.userAgent
}