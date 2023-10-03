package com.dreamsoftware.data.database.datasource.catchup.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.CatchupEntityDAO
import com.dreamsoftware.data.database.dao.CatchupTable
import com.dreamsoftware.data.database.datasource.catchup.ICatchupDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CatchupEntity
import com.dreamsoftware.data.database.entity.SaveCatchupEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder


/**
 * Implementation of [ICatchupDatabaseDataSource] for managing Catchup entities in the database.
 *
 * @param database The [IDatabaseFactory] used to access the database.
 * @param mapper The [ISimpleMapper] used to map Catchup entities to [CatchupEntity].
 */
internal class CatchupDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<CatchupEntityDAO, CatchupEntity>
) : SupportDatabaseDataSource<Int, CatchupEntityDAO, SaveCatchupEntity, CatchupEntity>(
    database,
    mapper,
    CatchupEntityDAO
), ICatchupDatabaseDataSource {

    /**
     * Maps a [SaveCatchupEntity] to an [UpdateBuilder] to prepare it for saving in the database.
     *
     * @param entityToSave The [SaveCatchupEntity] to be saved in the database.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveCatchupEntity) {
        // Map the catchup file path to the corresponding database column
        this@onMapEntityToSave[CatchupTable.storagePath] = entityToSave.catchupFilePath
    }
}