package com.dreamsoftware.data.iptvorg.datasource

import com.dreamsoftware.data.iptvorg.exception.NetworkErrorException
import kotlin.jvm.Throws

/**
 * Interface for a network data source that fetches content from an IPTV organization.
 *
 * @param T The type of content to be fetched.
 */
interface IptvOrgNetworkDataSource<out T> {

    /**
     * Fetches content from the IPTV organization.
     *
     * @return An iterable of the fetched content.
     * @throws NetworkErrorException If a network-related error occurs during the fetch.
     */
    @Throws(NetworkErrorException::class)
    suspend fun fetchContent(): Iterable<T>
}