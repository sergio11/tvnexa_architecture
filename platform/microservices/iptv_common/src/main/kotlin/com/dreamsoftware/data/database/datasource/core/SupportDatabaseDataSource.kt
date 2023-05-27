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
import org.slf4j.LoggerFactory

internal abstract class SupportDatabaseDataSource<KEY : Comparable<KEY>, DAO : Entity<KEY>, INPUT, OUTPUT>(
    private val database: IDatabaseFactory,
    private val mapper: IMapper<DAO, OUTPUT>,
    private val entityDAO: EntityClass<KEY, DAO>
) : ISupportDatabaseDataSource<KEY, INPUT, OUTPUT> {

    protected val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun findAll(): Iterable<OUTPUT> = dbExec {
        entityDAO.all().sortedByDescending { it.id }.map(mapper::map)
    }

    override suspend fun findByKey(key: KEY): OUTPUT = dbExec {
        entityDAO.findById(key)?.let(mapper::map) ?: throw RuntimeException()
    }

    override suspend fun save(data: INPUT) {
        dbExec {
            entityDAO.table.insertOnDuplicateKeyUpdate(
                onDupUpdateColumns = listOf(entityDAO.table.id),
                data = data
            ) { onMapEntityToSave(it) }
            commit()
            onSaveTransactionFinished(data)
        }
    }

    override suspend fun save(data: Iterable<INPUT>) = with(data) {
        dbExec {
            entityDAO.table.batchInsertOnDuplicateKeyUpdate(
                onDupUpdateColumns = listOf(entityDAO.table.id),
                itemsCount = count()
            ) { idx -> onMapEntityToSave(elementAt(idx)) }
            commit()
            onSaveTransactionFinished(data)
        }
    }

    override suspend fun deleteByKey(key: KEY): Unit = dbExec {
        entityDAO.table.deleteWhere { id eq key }
    }

    override suspend fun deleteAll(): Unit = dbExec { entityDAO.table.deleteAll() }

    abstract fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: INPUT)

    open fun Transaction.onSaveTransactionFinished(data: Iterable<INPUT>) {}

    open fun Transaction.onSaveTransactionFinished(data: INPUT) {}

    protected suspend fun <OUTPUT> dbExec(dbExecution: Transaction.() -> OUTPUT): OUTPUT = withContext(Dispatchers.IO) {
        database.dbExec { dbExecution() }
    }

    protected  fun <T : Table, E> T.insertOnDuplicateKeyUpdate(
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
        onSaveData: UpdateBuilder<Int>.(item: E) -> Unit = {}
    ) {
        with(data) {
            batchInsertOnDuplicateKeyUpdate(
                itemsCount = count(),
                onDupUpdateColumns = onDupUpdateColumns
            ) {
                onSaveData(elementAt(it))
            }
        }
    }

    private fun <T : Table> T.batchInsertOnDuplicateKeyUpdate(
        itemsCount: Int,
        onDupUpdateColumns: List<Column<*>>,
        onSaveData: UpdateBuilder<Int>.(idx: Int) -> Unit = {}
    ) {
        if (itemsCount > 0) {
            TransactionManager.current().exec(BatchInsertUpdateOnDuplicate(this, onDupUpdateColumns).apply {
                for (i in 0 until itemsCount) {
                    addBatch()
                    onSaveData(i)
                }
            })
        }
    }
}