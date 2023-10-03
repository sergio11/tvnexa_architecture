package com.dreamsoftware.data.database.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object CatchupTable : IntIdTable(name = "catchups") {
    // Storage path of the Catchup file in MinIO
    val storagePath = varchar(name = "storage_path", length = 255)
    // Date and time when the Catchup was created
    val dateCreated = datetime(name = "date_created")
    // Duration of the Catchup program in minutes
    val durationMinutes = integer(name = "duration_minutes")
    val epgChannelProgrammeId = reference(
        name = "epg_channel_programme_id",
        foreign = EpgChannelProgrammeTable,
        onDelete = ReferenceOption.CASCADE
    )
}

class CatchupEntityDAO(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, CatchupEntityDAO>(CatchupTable)

    var storagePath by CatchupTable.storagePath
    var dateCreated by CatchupTable.dateCreated
    var durationMinutes by CatchupTable.durationMinutes
    var epgChannelProgramme by EpgChannelProgrammeEntityDAO referencedOn CatchupTable.epgChannelProgrammeId
}
