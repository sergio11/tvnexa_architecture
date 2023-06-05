package com.dreamsoftware.data.epg.datasource

import com.dreamsoftware.data.epg.model.EpgDataDTO

interface IEpgGrabbingDataSource {

    suspend fun fetchEpgForSites(languageId: String, sites: Iterable<String>): Iterable<EpgDataDTO>
}