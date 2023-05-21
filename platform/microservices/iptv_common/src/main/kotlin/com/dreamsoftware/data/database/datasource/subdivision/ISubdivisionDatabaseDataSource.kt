package com.dreamsoftware.data.database.datasource.subdivision

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity

interface ISubdivisionDatabaseDataSource: ISupportDatabaseDataSource<String, SaveSubdivisionEntity, SubdivisionEntity>