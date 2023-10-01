package com.dreamsoftware.data.database.datasource.core

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.slf4j.LoggerFactory

internal abstract class SupportDatabaseDataSource<KEY : Comparable<KEY>, DAO : Entity<KEY>, INPUT, OUTPUT>(
    private val database: IDatabaseFactory,
    protected val mapper: ISimpleMapper<DAO, OUTPUT>,
    protected val entityDAO: EntityClass<KEY, DAO>
) : ISupportDatabaseDataSource<KEY, INPUT, OUTPUT> {

    private companion object {
        const val BATCH_SIZE = 1000
    }

    protected val log = LoggerFactory.getLogger(this::class.java)

    protected open val defaultBatchSize: Int
        get() = BATCH_SIZE

    override suspend fun findAll(): Iterable<OUTPUT> = execQuery {
        entityDAO.all().sortedByDescending { it.id }.map(mapper::map)
    }

    override suspend fun findByKey(key: KEY): OUTPUT = execQuery {
        entityDAO.findById(key)?.let(mapper::map) ?: throw RuntimeException()
    }

    override suspend fun save(data: INPUT) {
        execWrite {
            entityDAO.table.insertOnDuplicateKeyUpdate(
                onDupUpdateColumns = listOf(entityDAO.table.id),
                data = data
            ) { onMapEntityToSave(it) }
            onSaveTransactionFinished(data)
        }
    }

    override suspend fun save(data: Iterable<INPUT>) = with(data) {
        repeat(chunked(defaultBatchSize).size) {
            execWrite(disableFkValidations = true) {
                entityDAO.table.batchInsertOnDuplicateKeyUpdate(
                    onDupUpdateColumns = listOf(entityDAO.table.id),
                    itemsCount = count(),
                    onSuccess = {
                        onSaveTransactionFinished(data)
                    }
                ) { idx -> onMapEntityToSave(elementAt(idx)) }
            }
        }
    }

    override suspend fun deleteByKey(key: KEY): Unit = execWrite {
        entityDAO.table.deleteWhere { id eq key }
    }

    override suspend fun deleteAll(): Unit = execWrite { entityDAO.table.deleteAll() }

    abstract fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: INPUT)

    open fun Transaction.onSaveTransactionFinished(data: Iterable<INPUT>) {}

    open fun Transaction.onSaveTransactionFinished(data: INPUT) {}

    protected suspend fun <OUTPUT> execWrite(disableFkValidations: Boolean = false, dbExecution: Transaction.() -> OUTPUT): OUTPUT = withContext(Dispatchers.IO) {
        database.execWritableTransaction(disableFkValidations) {
            dbExecution()
        }
    }

    protected suspend fun <OUTPUT> execQuery(dbExecution: Transaction.() -> OUTPUT): OUTPUT = withContext(Dispatchers.IO) {
        database.execReadableTransaction {
            dbExecution()
        }
    }

    private fun <T : Table, E> T.insertOnDuplicateKeyUpdate(
        data: E,
        onDupUpdateColumns: List<Column<*>>,
        onSaveData: UpdateBuilder<Int>.(data: E) -> Unit = {}
    ) {
        TransactionManager.current().exec(InsertUpdateOnDuplicate<KEY>(this@insertOnDuplicateKeyUpdate, onDupUpdateColumns).apply {
            onSaveData(data)
        })
    }

    protected fun <T : Table, E> T.batchInsertOnDuplicateKeyUpdate(
        onDupUpdateColumns: List<Column<*>>,
        data: Iterable<E>,
        onSaveData: UpdateBuilder<Int>.(item: E) -> Unit = {},
        onSuccess: (insertedCount: Int) -> Unit = {}
    ) {
        with(data) {
            batchInsertOnDuplicateKeyUpdate(
                itemsCount = count(),
                onDupUpdateColumns = onDupUpdateColumns,
                onSuccess = onSuccess
            ) {
                onSaveData(elementAt(it))
            }
        }
    }

    private fun <T : Table> T.batchInsertOnDuplicateKeyUpdate(
        itemsCount: Int,
        onDupUpdateColumns: List<Column<*>>,
        onSuccess: (insertedCount: Int) -> Unit = {},
        onSaveData: UpdateBuilder<Int>.(idx: Int) -> Unit = {}
    ) {
        if (itemsCount > 0) {
            TransactionManager.current().apply {
                val batchInsert = BatchInsertUpdateOnDuplicate(this@batchInsertOnDuplicateKeyUpdate, onDupUpdateColumns).apply {
                    for (i in 0 until itemsCount) {
                        addBatch()
                        onSaveData(i)
                    }
                }
                exec(batchInsert)
                commit()
                onSuccess(batchInsert.insertedCount)
            }
        }
    }
}