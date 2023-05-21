package com.dreamsoftware.data.database.datasource.region

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity
import com.dreamsoftware.data.database.entity.SaveRegionEntity

interface IRegionDatabaseDataSource: ISupportDatabaseDataSource<String, SaveRegionEntity, RegionEntity>