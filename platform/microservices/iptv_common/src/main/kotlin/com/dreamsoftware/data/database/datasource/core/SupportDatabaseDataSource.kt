package com.dreamsoftware.data.database.datasource.core

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

internal abstract class SupportDatabaseDataSource<E, K : Comparable<K>>(
    private val database: IDatabaseFactory,
    private val mapper: IOneSideMapper<ResultRow, E>,
    private val table: IdTable<K>
): ISupportDatabaseDataSource<E, K> {

    override suspend fun findAll(): List<E> = withContext(Dispatchers.IO) {
        database.dbExec { table.selectAll().map(mapper::map) }
    }

    override suspend fun findByKey(key: K): E = withContext(Dispatchers.IO) {
        database.dbExec {
            with(table) {
                select { id eq key }.map(mapper::map).first()
            }
        }
    }

    override suspend fun save(entity: E) = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun save(entities: List<E>) = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteByKey(key: K): Unit = withContext(Dispatchers.IO) {
        database.dbExec { table.deleteWhere { id eq key } }
    }

    override suspend fun deleteAll(): Unit = withContext(Dispatchers.IO) {
        database.dbExec { table.deleteAll() }
    }
}