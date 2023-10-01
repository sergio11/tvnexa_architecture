package com.dreamsoftware.data.database.datasource.core

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.slf4j.LoggerFactory

/**
 * The `BatchInsertUpdateOnDuplicate` class extends `BatchInsertStatement` and provides support for batch inserting
 * records into a database table with an optional "ON DUPLICATE KEY UPDATE" clause to handle duplicate key conflicts.
 *
 * @param table The database table to insert records into.
 * @param onDupUpdate A list of columns to be updated in case of a duplicate key conflict.
 */
class BatchInsertUpdateOnDuplicate(table: Table, private val onDupUpdate: List<Column<*>>) : BatchInsertStatement(table, false) {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Prepares the SQL statement for batch insertion, including the "ON DUPLICATE KEY UPDATE" clause if applicable.
     *
     * @param transaction The database transaction context.
     * @return The prepared SQL statement.
     */
    override fun prepareSQL(transaction: Transaction): String {
        val onUpdateSQL = if (onDupUpdate.isNotEmpty()) {
            " ON DUPLICATE KEY UPDATE " + onDupUpdate.joinToString { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }
        } else ""
        return super.prepareSQL(transaction) + onUpdateSQL
    }
}