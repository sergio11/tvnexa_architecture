package com.dreamsoftware.data.database.datasource.core

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal abstract class SupportDatabaseDataSource<E, K : Comparable<K>>(
    private val database: IDatabaseFactory,
    private val mapper: IOneSideMapper<ResultRow, E>,
    private val table: IdTable<K>
): ISupportDatabaseDataSource<E, K> {

    override suspend fun findAll(): List<E> = dbExec { selectAll().map(mapper::map) }

    override suspend fun findByKey(key: K): E = dbExec { select { id eq key }.map(mapper::map).first() }

    override suspend fun save(entity: E) = dbExec {
        insertOnDuplicateKeyUpdate(entity, listOf(id))
    }

    override suspend fun save(entities: List<E>) = dbExec {
        batchInsertOnDuplicateKeyUpdate(entities, listOf(id))
    }

    override suspend fun deleteByKey(key: K): Unit = dbExec {
        deleteWhere { id eq key }
    }

    override suspend fun deleteAll(): Unit = dbExec { deleteAll() }

    abstract fun UpdateBuilder<Int>.onMapEntityToSave(entity: E)

    protected suspend fun <R>dbExec(dbExecution: IdTable<K>.() -> R): R = withContext(Dispatchers.IO) {
        database.dbExec { with(table) { dbExecution() } }
    }

    private fun IdTable<K>.insertOnDuplicateKeyUpdate(entity: E, onDupUpdateColumns: List<Column<*>>) {
        TransactionManager.current().exec(InsertUpdateOnDuplicate<K>(this, onDupUpdateColumns).apply {
            onMapEntityToSave(entity)
        })
    }

    private fun IdTable<K>.batchInsertOnDuplicateKeyUpdate(data: List<E>, onDupUpdateColumns: List<Column<*>>) {
        data.takeIf { it.isNotEmpty() }?.let {
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