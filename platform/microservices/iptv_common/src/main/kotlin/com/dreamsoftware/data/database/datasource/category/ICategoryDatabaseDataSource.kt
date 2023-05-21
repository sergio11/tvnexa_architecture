package com.dreamsoftware.data.database.datasource.category

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.SaveCategoryEntity

interface ICategoryDatabaseDataSource: ISupportDatabaseDataSource<String, SaveCategoryEntity, CategoryEntity>