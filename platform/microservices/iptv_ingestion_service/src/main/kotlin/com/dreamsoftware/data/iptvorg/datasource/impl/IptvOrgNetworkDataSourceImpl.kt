package com.dreamsoftware.data.iptvorg.datasource.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.iptvorg.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.exception.NetworkException
import com.dreamsoftware.data.iptvorg.exception.NetworkNoResultException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.reflect.*

internal class IptvOrgNetworkDataSourceImpl<in E, out T>(
    private val httpClient: HttpClient,
    private val mapper: IMapper<E, T>,
    private val resourceUrl: String,
    private val responseTypeInfo: TypeInfo
): SupportNetworkDataSource(), IptvOrgNetworkDataSource<T> {

    @Throws(NetworkException::class)
    override suspend fun fetchContent(): Iterable<T> = safeNetworkCall {
        val result: List<E> = httpClient.get(resourceUrl).body(responseTypeInfo)
        if(result.isEmpty())
            throw NetworkNoResultException("Not result found")
        mapper.mapList(result)
    }
}