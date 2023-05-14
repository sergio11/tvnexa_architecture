package com.dreamsoftware.data.iptvorg.datasource.country.impl

import com.dreamsoftware.data.iptvorg.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.country.ICountryNetworkDataSource
import com.dreamsoftware.model.IptvOrgConfig
import io.ktor.client.*

internal class CountryNetworkDataSourceImpl(
    private val httpClient: HttpClient,
    private val iptvOrgConfig: IptvOrgConfig
): SupportNetworkDataSource(), ICountryNetworkDataSource {
}