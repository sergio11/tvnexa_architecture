package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object EpgChannelProgrammeTable : LongIdTable(name = "epg_channel_programmes") {

    // Channel ID
    val channelId = varchar(name = "channel", length = 50).references(ChannelTable.channelId).nullable()

    // Program title
    val title = varchar(name = "title", length = 200)

    // Programme category
    val category = varchar(name = "category", length = 30).references(CategoryTable.categoryId).nullable()

    // Programme start
    val start = datetime(name = "start")

    // Programme end
    val end = datetime(name = "end")

    // Programme date
    val date = datetime(name = "date")

    // Program source domain name
    val site = varchar(name = "site", length = 100)

    // Language of the guide (ISO_639-1 code)
    val lang = char(name = "lang", length = 2)

}

class EpgChannelProgrammeEntityDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, EpgChannelProgrammeEntityDAO>(EpgChannelProgrammeTable)

    val channel by ChannelEntityDAO optionalReferencedOn EpgChannelProgrammeTable.channelId
    val category by CategoryEntityDAO optionalReferencedOn EpgChannelProgrammeTable.category
    var title by EpgChannelProgrammeTable.title
    var date by EpgChannelProgrammeTable.date
    var start by EpgChannelProgrammeTable.start
    var end by EpgChannelProgrammeTable.end
    var site by EpgChannelProgrammeTable.site
    var lang by EpgChannelProgrammeTable.lang
}