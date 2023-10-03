package com.dreamsoftware.data.database.datasource.catchup

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CatchupEntity
import com.dreamsoftware.data.database.entity.SaveCatchupEntity

/**
 * An interface for a Catchup Database Data Source.
 * This interface provides methods to interact with the database
 * for managing Catchup-related data.
 *
 * @param KEY The type of the primary key for Catchup entities.
 * @param INPUT The input data type used to save Catchup data.
 * @param OUTPUT The output data type representing Catchup entities.
 */
interface ICatchupDatabaseDataSource : ISupportDatabaseDataSource<Int, SaveCatchupEntity, CatchupEntity>