package com.dreamsoftware.data.database.datasource.subdivision

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.dao.SubdivisionEntityDAO
import com.dreamsoftware.data.database.entity.SubdivisionEntity

interface ISubdivisionDatabaseDataSource: ISupportDatabaseDataSource<SubdivisionEntityDAO, String, SubdivisionEntity>