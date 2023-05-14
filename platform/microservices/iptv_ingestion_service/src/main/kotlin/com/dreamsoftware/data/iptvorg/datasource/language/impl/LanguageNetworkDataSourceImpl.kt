package com.dreamsoftware.data.iptvorg.datasource.language.impl

import com.dreamsoftware.data.iptvorg.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.language.ILanguageNetworkDataSource
import com.dreamsoftware.model.IptvOrgConfig
import io.ktor.client.*

internal class LanguageNetworkDataSourceImpl(
    private val httpClient: HttpClient,
    private val iptvOrgConfig: IptvOrgConfig
): SupportNetworkDataSource(), ILanguageNetworkDataSource {


}