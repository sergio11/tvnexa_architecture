package com.dreamsoftware.data.database.datasource.core

import org.jetbrains.exposed.dao.Entity

interface ISupportDatabaseDataSource<E : Entity<K>, K : Comparable<K>, R> {

    suspend fun findAll(): List<R>

    suspend fun findByKey(key: K): R

    suspend fun save(entityToSave: R)

    suspend fun save(entitiesToSave: List<R>)

    suspend fun deleteByKey(key: K)

    suspend fun deleteAll()

}