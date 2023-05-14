package com.dreamsoftware.data.iptvorg.datasource.subdivision.impl

import com.dreamsoftware.data.iptvorg.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.subdivision.ISubdivisionNetworkDataSource
import com.dreamsoftware.model.IptvOrgConfig
import io.ktor.client.*

internal class SubdivisionNetworkDataSourceImpl(
    private val httpClient: HttpClient,
    private val iptvOrgConfig: IptvOrgConfig
): SupportNetworkDataSource(), ISubdivisionNetworkDataSource {
}