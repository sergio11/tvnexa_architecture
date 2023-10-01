package com.dreamsoftware.data.epg.datasource

import com.dreamsoftware.data.epg.model.EpgDataDTO

/**
 * Interface representing a data source for fetching Electronic Program Guide (EPG) data.
 * EPG data typically includes information about TV channel programs, schedules, and details.
 */
interface IEpgGrabbingDataSource {

    /**
     * Fetches EPG data for a list of sites and a specified language.
     *
     * @param languageId The language identifier for which EPG data is to be fetched.
     * @param sites An iterable collection of site names for which EPG data is to be retrieved.
     * @return An iterable collection of [EpgDataDTO] objects representing the fetched EPG data.
     */
    suspend fun fetchEpgForSites(languageId: String, sites: Iterable<String>): Iterable<EpgDataDTO>
}