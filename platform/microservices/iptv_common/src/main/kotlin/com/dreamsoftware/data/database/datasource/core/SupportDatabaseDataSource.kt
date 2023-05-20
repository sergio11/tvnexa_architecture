package com.dreamsoftware.data.database.datasource.core

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal abstract class SupportDatabaseDataSource<E : Entity<K>, K : Comparable<K>, R>(
    private val database: IDatabaseFactory,
    private val mapper: IMapper<E, R>,
    private val entityDAO: EntityClass<K, E>
) : ISupportDatabaseDataSource<E, K, R> {

    override suspend fun findAll(): List<R> = dbExec {
        all().sortedByDescending{ it.id }.map(mapper::map)
    }

    override suspend fun findByKey(key: K): R = dbExec {
        findById(key)?.let(mapper::map) ?: throw RuntimeException()
    }

    override suspend fun save(entityToSave: R) = dbExec {
        with(table) {
            insertOnDuplicateKeyUpdate(entityToSave, listOf(id))
        }
    }

    override suspend fun save(entitiesToSave: Iterable<R>) = dbExec {
        with(table) {
            batchInsertOnDuplicateKeyUpdate(entitiesToSave, listOf(id))
        }
    }

    override suspend fun deleteByKey(key: K): Unit = dbExec {
        table.deleteWhere { id eq key }
    }

    override suspend fun deleteAll(): Unit = dbExec { table.deleteAll() }

    abstract fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: R)

    protected suspend fun <R> dbExec(dbExecution: EntityClass<K, E>.() -> R): R = withContext(Dispatchers.IO) {
        database.dbExec { with(entityDAO) { dbExecution() } }
    }

    private fun IdTable<K>.insertOnDuplicateKeyUpdate(data: R, onDupUpdateColumns: List<Column<*>>) {
        TransactionManager.current().exec(InsertUpdateOnDuplicate<K>(this, onDupUpdateColumns).apply {
            onMapEntityToSave(data)
        })
    }

    private fun IdTable<K>.batchInsertOnDuplicateKeyUpdate(data: Iterable<R>, onDupUpdateColumns: List<Column<*>>) {
        data.takeIf { it.toList().isNotEmpty() }?.let {
            with(BatchInsertUpdateOnDuplicate(this, onDupUpdateColumns)) {
                data.forEach {
                    addBatch()
                    onMapEntityToSave(it)
                }
                TransactionManager.current().exec(this)
            }
        }
    }
}