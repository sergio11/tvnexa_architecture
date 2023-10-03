package com.dreamsoftware.data.database.dao


import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object ChannelTable: IdTable<String>(name = "channels") {

    // Unique channel ID
    val channelId = varchar(name = "id", length = 50)
    // Full name of the channel
    val name = varchar(name = "name", length = 50).uniqueIndex().nullable()
    // Name of the network operating the channel
    val network = varchar(name = "network", length = 50).nullable()
    // Country code from which the broadcast is transmitted (ISO 3166-1 alpha-2)
    val country = char(name = "country", length = 2).references(CountryTable.code)
    // Code of the subdivision (e.g., provinces or states) from which the broadcast is transmitted (ISO 3166-2)
    val subdivision = varchar(name = "subdivision", length = 10).references(SubdivisionTable.code).nullable()
    // Name of the city from which the broadcast is transmitted
    val city = varchar(name = "city", length = 50).nullable()
    // Indicates whether the channel broadcasts adult content
    val isNsfw = bool(name = "is_nsfw").nullable()
    // Launch date of the channel (YYYY-MM-DD)
    val launched = char(name = "launched", length = 10).nullable()
    // Date on which the channel closed (YYYY-MM-DD)
    val closed = char(name = "closed", length = 10).nullable()
    // The ID of the channel that this channel was replaced by
    val replacedBy = varchar(name = "replacedBy", length = 50).references(channelId).nullable()
    // Official website URL
    val website = varchar(name = "website", length = 1000).nullable()
    // Logo URL
    val logo = varchar(name = "logo", length = 1000).nullable()
    // Indicates whether the channel allows Catchup content generation
    val catchupEnabled = bool(name = "catchup_enabled").default(false)

    override val id: Column<EntityID<String>> = channelId.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

// List of alternative channel names
object ChannelNameTable: LongIdTable(name = "channels_alt_names") {

    val channel = reference("channel", ChannelTable)
    val altName = varchar(name = "name", length = 100)

    init {
        uniqueIndex("UNIQUE_ChannelName", channel, altName)
    }
}

// List of channel owners
object ChannelOwnerTable: LongIdTable(name = "channels_owners") {

    val channel = reference(name = "channel", ChannelTable)
    val owner = varchar(name = "owner", length = 500)

    init {
        uniqueIndex("UNIQUE_ChannelOwner", channel, owner)
    }
}

// List of codes describing the broadcasting area (r/<region_code>, c/<country_code>, s/<subdivision_code>)
object ChannelBroadcastAreaTable: LongIdTable(name = "channels_broadcast_areas") {

    val channel = reference("channel", ChannelTable)
    val broadcastArea = varchar(name = "broadcast_area", length = 50)

    init {
        uniqueIndex("UNIQUE_ChannelBroadcastArea", channel, broadcastArea)
    }
}

// List of languages broadcast
object ChannelLanguageTable: LongIdTable(name = "channels_languages") {

    val channel = reference(name = "channel", ChannelTable)
    val language = reference(name = "language", LanguageTable)

    init {
        uniqueIndex("UNIQUE_ChannelLanguage", channel, language)
    }
}

// List of categories to which this channel belongs
object ChannelCategoryTable: LongIdTable(name = "channels_categories") {

    val channel = reference("channel", ChannelTable)
    val category = reference("category", CategoryTable)

    init {
        uniqueIndex("UNIQUE_ChannelCategory", channel, category)
    }
}


class ChannelEntityDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ChannelEntityDAO>(ChannelTable)

    var channelId by ChannelTable.channelId
    var name by ChannelTable.name
    var network by ChannelTable.network
    var country by CountryEntityDAO referencedOn ChannelTable.country
    var subdivision by SubdivisionEntityDAO optionalReferencedOn ChannelTable.subdivision
    var city by ChannelTable.city
    var isNsfw by ChannelTable.isNsfw
    var website by ChannelTable.website
    var logo by ChannelTable.logo
    var launched by ChannelTable.launched
    var closed by ChannelTable.closed
    var catchupEnabled by ChannelTable.catchupEnabled
    var replacedBy by ChannelEntityDAO optionalReferencedOn ChannelTable.replacedBy
    val languages by LanguageEntityDAO referrersOn ChannelLanguageTable.channel
    val categories by CategoryEntityDAO referrersOn ChannelCategoryTable.channel
    val stream by ChannelStreamEntityDAO optionalBackReferencedOn ChannelStreamTable.channelId
}
