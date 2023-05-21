package com.dreamsoftware.data.database.datasource.core


interface ISupportDatabaseDataSource<KEY : Comparable<KEY>, INPUT, OUTPUT> {

    suspend fun findAll(): Iterable<OUTPUT>

    suspend fun findByKey(key: KEY): OUTPUT

    suspend fun save(data: INPUT)

    suspend fun save(data: Iterable<INPUT>)

    suspend fun deleteByKey(key: KEY)

    suspend fun deleteAll()

}