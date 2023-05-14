package com.dreamsoftware.data.iptvorg.datasource.category.impl

import com.dreamsoftware.data.iptvorg.datasource.category.ICategoryNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.model.IptvOrgConfig
import io.ktor.client.*

internal class CategoryNetworkDataSourceImpl(
    private val httpClient: HttpClient,
    private val iptvOrgConfig: IptvOrgConfig
): SupportNetworkDataSource(),  ICategoryNetworkDataSource {
}