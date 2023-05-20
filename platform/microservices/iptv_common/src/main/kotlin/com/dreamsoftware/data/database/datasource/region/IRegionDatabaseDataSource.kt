package com.dreamsoftware.data.database.datasource.region

import com.dreamsoftware.data.database.dao.RegionEntityDAO
import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity

interface IRegionDatabaseDataSource: ISupportDatabaseDataSource<RegionEntityDAO, String, RegionEntity>