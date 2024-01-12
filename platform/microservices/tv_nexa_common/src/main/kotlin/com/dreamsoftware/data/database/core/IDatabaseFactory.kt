package com.dreamsoftware.data.database.core

import org.jetbrains.exposed.sql.Transaction

/**
 * The `IDatabaseFactory` interface defines methods for managing database connections and transactions.
 */
interface IDatabaseFactory {
    /**
     * Connects to the database and performs schema migration.
     */
    fun connectAndMigrate()

    /**
     * Executes a database read operation within a transaction.
     *
     * @param block The database read operation to execute within the transaction.
     * @return The result of the database read operation.
     */
    suspend fun <T> execReadableTransaction(block: Transaction.() -> T): T

    /**
     * Executes a database write operation within a transaction.
     *
     * @param disableFkValidations Flag to disable foreign key validations during the transaction (default is false).
     * @param block The database write operation to execute within the transaction.
     * @return The result of the database write operation.
     */
    suspend fun <T> execWritableTransaction(
        disableFkValidations: Boolean = false,
        block: Transaction.() -> T
    ): T
}