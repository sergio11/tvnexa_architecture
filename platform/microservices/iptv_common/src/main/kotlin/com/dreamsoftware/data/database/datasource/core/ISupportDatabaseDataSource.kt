package com.dreamsoftware.data.database.datasource.core

/**
 * The `ISupportDatabaseDataSource` interface defines a set of common database operations that can be performed on a data source.
 *
 * @param KEY The type of key used for data retrieval.
 * @param INPUT The type of data to be saved or processed.
 * @param OUTPUT The type of data to be returned from data retrieval operations.
 */
interface ISupportDatabaseDataSource<KEY : Comparable<KEY>, INPUT, OUTPUT> {

    /**
     * Retrieves all data records from the data source.
     *
     * @return An iterable collection of output data records.
     */
    suspend fun findAll(): Iterable<OUTPUT>

    /**
     * Retrieves a specific data record from the data source based on its key.
     *
     * @param key The key used to identify the data record.
     * @return The output data record associated with the key.
     */
    suspend fun findByKey(key: KEY): OUTPUT

    /**
     * Saves a single data record in the data source.
     *
     * @param data The input data record to be saved.
     */
    suspend fun save(data: INPUT)

    /**
     * Saves multiple data records in the data source.
     *
     * @param data A collection of input data records to be saved.
     */
    suspend fun save(data: Iterable<INPUT>)

    /**
     * Deletes a specific data record from the data source based on its key.
     *
     * @param key The key used to identify the data record to be deleted.
     */
    suspend fun deleteByKey(key: KEY)

    /**
     * Deletes all data records from the data source.
     */
    suspend fun deleteAll()
}