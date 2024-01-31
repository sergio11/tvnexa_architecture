package com.dreamsoftware.data.database.datasource.core

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty1

/**
 * An abstract base class for supporting database data sources.
 * This class provides common database operations like finding, saving, and deleting records.
 *
 * @param KEY The type of the primary key for the database entity.
 * @param DAO The type of the database entity.
 * @param INPUT The input data type.
 * @param OUTPUT The output data type.
 * @param database The IDatabaseFactory used to access the database.
 * @param mapper The ISimpleMapper used to map database entities to OUTPUT.
 * @param entityDAO The EntityClass used to interact with the database.
 */
internal abstract class SupportDatabaseDataSource<KEY : Comparable<KEY>, DAO : Entity<KEY>, INPUT, OUTPUT>(
    private val database: IDatabaseFactory,
    protected val mapper: ISimpleMapper<DAO, OUTPUT>,
    protected val entityDAO: EntityClass<KEY, DAO>
) : ISupportDatabaseDataSource<KEY, INPUT, OUTPUT> {

    private companion object {
        const val BATCH_SIZE = 1000
    }

    protected val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Gets the default batch size for batch operations.
     */
    protected open val defaultBatchSize: Int
        get() = BATCH_SIZE

    protected open val disableFkValidationsOnBatchOperation: Boolean
        get() = false

    protected open val findByEagerRelationships: List<KProperty1<DAO, Any?>>
        get() = emptyList()

    protected open val findAllEagerRelationships: List<KProperty1<DAO, Any?>>
        get() = emptyList()

    /**
     * Finds all records in the database and returns them as an iterable of OUTPUT.
     *
     * @return An iterable of OUTPUT containing all records.
     */
    override suspend fun findAll(): Iterable<OUTPUT> = execQuery {
        onBuildFindAllQuery(eagerRelationships = findAllEagerRelationships).map(mapper::map)
    }

    /**
     * Retrieves a paginated list of records from the database and maps them to an iterable of OUTPUT.
     *
     * @param offset The offset indicating the starting point from where records should be fetched.
     * @param limit The maximum number of records to be retrieved.
     * @return An iterable of OUTPUT containing a paginated list of records.
     */
    override suspend fun findPaginated(offset: Long, limit: Int): Iterable<OUTPUT> = execQuery {
        onBuildFindPaginatedQuery(limit, offset, eagerRelationships = findAllEagerRelationships).map(mapper::map)
    }

    /**
     * Finds a record by its primary key and returns it as OUTPUT.
     *
     * @param key The primary key of the record to retrieve.
     * @return The retrieved record as OUTPUT.
     */
    override suspend fun findByKey(key: KEY): OUTPUT? = execQuery {
        onBuildFindByKeyQuery(key, eagerRelationships = findByEagerRelationships)?.let(mapper::map)
    }

    /**
     * Saves a single record in the database.
     *
     * @param data The input data to save as a new record or update an existing one.
     */
    override suspend fun save(data: INPUT) {
        execWrite {
            entityDAO.table.insertOnDuplicateKeyUpdate(
                onDupUpdateColumns = listOf(entityDAO.table.id),
                data = data
            ) { onMapEntityToSave(it) }
            onSaveTransactionFinished(data)
        }
    }

    /**
     * Saves a batch of records in the database.
     *
     * @param data An iterable of input data to save as new records or update existing ones.
     */
    override suspend fun save(data: Iterable<INPUT>) = with(data) {
        chunked(defaultBatchSize).forEach { chunk ->
            log.debug("save chunk size ${chunk.count()}")
            execWrite(disableFkValidations = disableFkValidationsOnBatchOperation) {
                entityDAO.table.batchInsertOnDuplicateKeyUpdate(
                    onDupUpdateColumns = listOf(entityDAO.table.id),
                    itemsCount = chunk.count(),
                    onSuccess = {
                        onSaveTransactionFinished(chunk)
                    }
                ) { idx -> onMapEntityToSave(chunk.elementAt(idx)) }
            }
        }
    }

    /**
     * Deletes a record from the database by its primary key.
     *
     * @param key The primary key of the record to delete.
     */
    override suspend fun deleteByKey(key: KEY): Unit = execWrite {
        entityDAO.table.deleteWhere { id eq key }
    }

    /**
     * Deletes all records from the database.
     */
    override suspend fun deleteAll(): Unit = execWrite { entityDAO.table.deleteAll() }

    /**
     * This method should be implemented to map an entity to the format suitable for saving in the database.
     *
     * @param entityToSave The entity to be saved in the database.
     */
    abstract fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: INPUT)

    open fun onSortBy(dao: DAO): String = dao.id.value.toString()

    /**
     * This method is called after a transaction for saving a single record finishes.
     *
     * @param data The input data that was saved.
     */
    open fun Transaction.onSaveTransactionFinished(data: INPUT) {}

    /**
     * This method is called after a transaction for batch saving finishes.
     *
     * @param data The iterable of input data that was saved.
     */
    open fun Transaction.onSaveTransactionFinished(data: Iterable<INPUT>) {}

    /**
     * Executes a write transaction with optional foreign key validations disabled.
     *
     * @param disableFkValidations Set to true to disable foreign key validations during the transaction.
     * @param dbExecution The transaction logic to execute.
     * @return The result of the transaction execution.
     */
    protected suspend fun <OUTPUT> execWrite(disableFkValidations: Boolean = false, dbExecution: Transaction.() -> OUTPUT): OUTPUT = withContext(Dispatchers.IO) {
        database.execWritableTransaction(disableFkValidations) {
            dbExecution()
        }
    }

    /**
     * Executes a read-only transaction.
     *
     * @param dbExecution The transaction logic to execute.
     * @return The result of the transaction execution.
     */
    protected suspend fun <OUTPUT> execQuery(dbExecution: Transaction.() -> OUTPUT): OUTPUT = withContext(Dispatchers.IO) {
        database.execReadableTransaction {
            dbExecution()
        }
    }

    /**
     * Constructs and retrieves a paginated query to find entities based on a specified [limit] and [offset],
     * with specified [eagerRelationships] loaded.
     *
     * @param limit The maximum number of entities to retrieve.
     * @param offset The starting point within the dataset to retrieve entities.
     * @param eagerRelationships The list of eager relationships to load alongside the entities (default: empty list).
     * @return A query for paginated retrieval of entities with eager relationships loaded.
     */
    protected fun onBuildFindPaginatedQuery(limit: Int, offset: Long, eagerRelationships: List<KProperty1<DAO, Any?>> = emptyList()) =
        entityDAO.all()
            .limit(limit, offset = offset)
            .with(*eagerRelationships.toTypedArray())
            .sortedBy(::onSortBy)

    /**
     * Constructs and retrieves a query to find all entities with eager relationships loaded.
     *
     * @param eagerRelationships The list of eager relationships to load alongside the entities (default: empty list).
     * @return A query for retrieval of all entities with eager relationships loaded.
     */
    protected fun onBuildFindAllQuery(eagerRelationships: List<KProperty1<DAO, Any?>> = emptyList()) =
        entityDAO.all()
            .with(*eagerRelationships.toTypedArray())
            .sortedBy(::onSortBy)

    /**
     * Constructs and retrieves a query to find an entity by its unique [key] with eager relationships loaded.
     *
     * @param key The unique identifier used to find the entity.
     * @param eagerRelationships The list of eager relationships to load alongside the entity (default: empty list).
     * @return A query to find an entity by its key with eager relationships loaded, or null if not found.
     */
    protected fun onBuildFindByKeyQuery(key: KEY, eagerRelationships: List<KProperty1<DAO, Any?>> = emptyList()) =
        entityDAO.findById(key)?.load(*eagerRelationships.toTypedArray())

    /**
     * Inserts a record into the database table with an update on duplicate key.
     *
     * @param data The data to insert or update.
     * @param onDupUpdateColumns The list of columns to update on duplicate key.
     * @param onSaveData A function to map and save the data.
     */
    private fun <T : Table, E> T.insertOnDuplicateKeyUpdate(
        data: E,
        onDupUpdateColumns: List<Column<*>>,
        onSaveData: UpdateBuilder<Int>.(data: E) -> Unit = {}
    ) {
        TransactionManager.current().exec(InsertUpdateOnDuplicate<KEY>(this@insertOnDuplicateKeyUpdate, onDupUpdateColumns).apply {
            onSaveData(data)
        })
    }

    /**
     * Inserts a batch of records into the database table with an update on duplicate key.
     *
     * @param onDupUpdateColumns The list of columns to update on duplicate key.
     * @param data The iterable of data to insert or update.
     * @param onSaveData A function to map and save each item in the batch.
     * @param onSuccess A callback function to execute after the batch insert is successful.
     */
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

    /**
     * Inserts a batch of records into the database table with an update on duplicate key.
     *
     * @param itemsCount The number of items in the batch.
     * @param onDupUpdateColumns The list of columns to update on duplicate key.
     * @param onSuccess A callback function to execute after the batch insert is successful.
     * @param onSaveData A function to map and save each item in the batch.
     */
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