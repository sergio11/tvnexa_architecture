package com.dreamsoftware.data.iptvorg.datasource

import com.dreamsoftware.data.iptvorg.exception.NetworkErrorException
import kotlin.jvm.Throws

interface IptvOrgNetworkDataSource<out T> {
    @Throws(NetworkErrorException::class)
    suspend fun fetchContent(): Iterable<T>
}