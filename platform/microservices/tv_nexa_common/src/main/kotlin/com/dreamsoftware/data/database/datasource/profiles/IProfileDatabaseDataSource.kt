package com.dreamsoftware.data.database.datasource.profiles

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.*
import java.util.*

interface IProfileDatabaseDataSource : ISupportDatabaseDataSource<UUID, CreateProfileEntity, ProfileEntity> {

}