package com.dreamsoftware.data.database.datasource.core

interface ISupportDatabaseDataSource<E, K : Comparable<K>> {

    suspend fun findAll(): List<E>

    suspend fun findByKey(key: K): E

    suspend fun save(entity: E)

    suspend fun save(entities: List<E>)

    suspend fun deleteByKey(key: K)

    suspend fun deleteAll()

}