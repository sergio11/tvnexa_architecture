package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ChannelGuideTable: LongIdTable(name = "channel_guides") {

    // Channel ID
    val channel = varchar(name = "channel", length = 50).references(ChannelTable.channelId)
    // Program source domain name
    val site = varchar(name = "site", length = 100)
    // Unique channel ID used on the site.
    val siteId = varchar(name = "site_id", length = 100).uniqueIndex()
    // Channel name used on the site
    val siteName = varchar(name = "site_name", length = 100)
    // Language of the guide (ISO_639-1 code)
    val lang = char(name = "lang", length = 2)
}

class ChannelGuideEntityDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, ChannelGuideEntityDAO>(ChannelGuideTable)

    var channel by ChannelEntityDAO referencedOn ChannelGuideTable.channel
    var site by ChannelGuideTable.site
    var siteId by ChannelGuideTable.siteId
    var siteName by ChannelGuideTable.siteName
    var lang by ChannelGuideTable.lang
}