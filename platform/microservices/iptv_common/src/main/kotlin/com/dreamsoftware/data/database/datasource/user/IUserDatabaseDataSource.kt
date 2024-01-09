package com.dreamsoftware.data.database.datasource.user

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveUserEntity
import com.dreamsoftware.data.database.entity.UserEntity

interface IUserDatabaseDataSource: ISupportDatabaseDataSource<Long, SaveUserEntity, UserEntity>