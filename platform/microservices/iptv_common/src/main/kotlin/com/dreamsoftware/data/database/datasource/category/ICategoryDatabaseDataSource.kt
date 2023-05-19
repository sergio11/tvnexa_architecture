package com.dreamsoftware.data.database.datasource.category

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.entity.CategoryEntity

interface ICategoryDatabaseDataSource: ISupportDatabaseDataSource<CategoryEntityDAO, String, CategoryEntity>