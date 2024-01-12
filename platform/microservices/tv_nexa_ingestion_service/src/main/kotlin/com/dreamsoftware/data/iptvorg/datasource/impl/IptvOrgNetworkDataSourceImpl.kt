package com.dreamsoftware.data.iptvorg.datasource.impl

import com.dreamsoftware.data.iptvorg.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.exception.NetworkException
import com.dreamsoftware.data.iptvorg.exception.NetworkNoResultException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.reflect.*

/**
 * Implementation of the [IptvOrgNetworkDataSource] interface for fetching content
 * from an IPTV data source using the Iptv.org API.
 *
 * @param T The type of content to fetch.
 * @property httpClient The [HttpClient] instance used for making HTTP requests.
 * @property resourceUrl The URL of the API resource to fetch content from.
 * @property responseTypeInfo [TypeInfo] representing the type information of the expected response.
 */
internal class IptvOrgNetworkDataSourceImpl<out T>(
    private val httpClient: HttpClient,
    private val resourceUrl: String,
    private val responseTypeInfo: TypeInfo
): SupportNetworkDataSource(), IptvOrgNetworkDataSource<T> {

    /**
     * Fetches content from the IPTV data source and returns it as an iterable of type [T].
     *
     * @return Iterable of type [T] representing the fetched content.
     * @throws NetworkException if there is an issue with the network request.
     * @throws NetworkNoResultException if no results are found in the response.
     */
    @Throws(NetworkException::class)
    override suspend fun fetchContent(): Iterable<T> = safeNetworkCall {
        httpClient.get(resourceUrl).body<List<T>>(responseTypeInfo).also {
            if(it.isEmpty())
                throw NetworkNoResultException("Not result found")
        }
    }
}