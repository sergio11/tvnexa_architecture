package com.dreamsoftware.data.database.datasource.core

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.slf4j.LoggerFactory

class BatchInsertUpdateOnDuplicate(table: Table, private val onDupUpdate: List<Column<*>>) : BatchInsertStatement(table, false) {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun prepareSQL(transaction: Transaction): String {
        val onUpdateSQL = if (onDupUpdate.isNotEmpty()) {
            " ON DUPLICATE KEY UPDATE " + onDupUpdate.joinToString { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }
        } else ""
        return super.prepareSQL(transaction) + onUpdateSQL
    }

}