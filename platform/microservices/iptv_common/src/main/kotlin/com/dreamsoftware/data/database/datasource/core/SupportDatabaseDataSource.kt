package com.dreamsoftware.data.database.datasource.core

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal abstract class SupportDatabaseDataSource<KEY : Comparable<KEY>, DAO : Entity<KEY>, INPUT, OUTPUT>(
    private val database: IDatabaseFactory,
    private val mapper: IMapper<DAO, OUTPUT>,
    private val entityDAO: EntityClass<KEY, DAO>
) : ISupportDatabaseDataSource<KEY, INPUT, OUTPUT> {

    override suspend fun findAll(): Iterable<OUTPUT> = dbExec {
        all().sortedByDescending { it.id }.map(mapper::map)
    }

    override suspend fun findByKey(key: KEY): OUTPUT = dbExec {
        findById(key)?.let(mapper::map) ?: throw RuntimeException()
    }

    override suspend fun save(data: INPUT) {
        insertOnDuplicateKeyUpdate { onMapEntityToSave(data) }
    }

    override suspend fun save(data: Iterable<INPUT>) = with(data) {
        batchInsertOnDuplicateKeyUpdate(itemsCount = count()) { idx -> onMapEntityToSave(elementAt(idx)) }
    }

    override suspend fun deleteByKey(key: KEY): Unit = dbExec {
        table.deleteWhere { id eq key }
    }

    override suspend fun deleteAll(): Unit = dbExec { table.deleteAll() }

    abstract fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: INPUT): Unit

    protected suspend fun <OUTPUT> dbExec(dbExecution: EntityClass<KEY, DAO>.() -> OUTPUT): OUTPUT = withContext(Dispatchers.IO) {
        database.dbExec { with(entityDAO) { dbExecution() } }
    }

    protected suspend fun insertOnDuplicateKeyUpdate(onSaveData: UpdateBuilder<Int>.() -> Unit = {}): Unit = dbExec {
        TransactionManager.current().exec(InsertUpdateOnDuplicate<KEY>(table, listOf(table.id)).apply {
            onSaveData()
        })
    }

    protected suspend fun <T> batchInsertOnDuplicateKeyUpdate(data: Iterable<T>, onSaveData: UpdateBuilder<Int>.(item: T) -> Unit = {}) {
        with(data) {
            batchInsertOnDuplicateKeyUpdate(count()) { idx ->
                onSaveData(elementAt(idx))
            }
        }
    }

    private suspend fun batchInsertOnDuplicateKeyUpdate(
        itemsCount: Int,
        onSaveData: UpdateBuilder<Int>.(idx: Int) -> Unit = {}
    ): Unit = dbExec {
        if (itemsCount > 0) {
            with(BatchInsertUpdateOnDuplicate(table, listOf(table.id))) {
                for (i in 0 until itemsCount) {
                    addBatch()
                    onSaveData(i)
                }
                TransactionManager.current().exec(this)
            }
        }
    }
}