package com.dreamsoftware.data.epg.datasource

import com.dreamsoftware.data.epg.model.EpgDataDTO

/**
 * Interface representing a data source for fetching Electronic Program Guide (EPG) data.
 * EPG data typically includes information about TV channel programs, schedules, and details.
 */
interface IEpgGrabbingDataSource {

    /**
     * Fetches Electronic Program Guide (EPG) data for a specific site and language.
     *
     * @param languageId The identifier for the desired language (e.g., ISO_639-1 code).
     * @param site The specific site or source from which to retrieve EPG data.
     * @return An iterable collection of [EpgDataDTO] containing EPG data for the given sites and language.
     */
    suspend fun fetchEpgByLanguageAndSite(languageId: String, site: String): Iterable<EpgDataDTO>
}