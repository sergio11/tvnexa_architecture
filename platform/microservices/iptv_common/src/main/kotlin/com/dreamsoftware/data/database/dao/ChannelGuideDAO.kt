package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ChannelGuideTable: LongIdTable(name = "channel_guides") {

    // Channel ID
    val channelId = varchar(name = "channel", length = 50).references(ChannelTable.channelId)
    // Program source domain name
    val site = varchar(name = "site", length = 100)
    // Language of the guide (ISO_639-1 code)
    val lang = char(name = "lang", length = 2)
    // Number of days for which this guide is intended
    val days = integer(name = "days")
}

class ChannelGuideEntityDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, ChannelGuideEntityDAO>(ChannelGuideTable)

    val channel by ChannelEntityDAO referencedOn ChannelTable.channelId
    var site by ChannelGuideTable.site
    var lang by ChannelGuideTable.lang
    var days by ChannelGuideTable.days
}