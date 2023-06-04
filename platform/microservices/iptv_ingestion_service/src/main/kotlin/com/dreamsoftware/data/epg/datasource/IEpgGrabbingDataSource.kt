package com.dreamsoftware.data.epg.datasource

import com.dreamsoftware.data.epg.model.TestEpgDTO

interface IEpgGrabbingDataSource {

    suspend fun fetchEpgForSites(languageId: String, sites: Iterable<String>): Iterable<TestEpgDTO>
}