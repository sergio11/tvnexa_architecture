package com.dreamsoftware.data.epg.datasource.impl

import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.data.epg.model.EpgDTO
import com.dreamsoftware.data.epg.model.EpgDataDTO
import com.dreamsoftware.model.EpgGrabbingConfig
import com.dreamsoftware.utils.getFinalOutputGuidesPath
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * Implementation of [IEpgGrabbingDataSource] for fetching Electronic Program Guide (EPG) data.
 * This class retrieves EPG data for specified sites and language using an external EPG grabber tool.
 *
 * @param xmlMapper An instance of ObjectMapper for handling XML data.
 * @param epgGrabbingConfig Configuration settings for EPG grabbing.
 */
internal class EpgGrabbingDataSourceImpl(
    private val xmlMapper: ObjectMapper,
    private val epgGrabbingConfig: EpgGrabbingConfig
) : IEpgGrabbingDataSource {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Constant representing a failed result code for the EPG grabber tool.
     */
    private companion object {
        const val EPG_GRABBER_FAILED = 1
        const val EPG_GRABBER_SUCCESS = 0
        const val EPG_GRABBER_MAX_TIMEOUT_MINUTES = 60L
    }

    /**
     * Fetches Electronic Program Guide (EPG) data for a specific site and language.
     *
     * @param languageId The identifier for the desired language (e.g., ISO_639-1 code).
     * @param site The specific site or source from which to retrieve EPG data.
     * @return An iterable collection of [EpgDataDTO] containing EPG data for the given sites and language.
     */
    override suspend fun fetchEpgByLanguageAndSite(languageId: String, site: String): Iterable<EpgDataDTO> =
        // Coroutine context switched to IO dispatcher for IO-bound operations
        fetchEpgResults(languageId, site).let {
            if(it.second != EPG_GRABBER_FAILED) {
                log.debug("fetchEpgForSites - languageId: $languageId - site: $site starting map epg data from guide file")
                mapEpgDataFromGuideFile(languageId, site)
            } else {
                log.debug("fetchEpgForSites - languageId: $languageId - site: $site EPG grabber failed")
                emptyList()
            }
        }

    /**
     * Maps Electronic Program Guide (EPG) data from a guide file for a specific language and site.
     *
     * @param languageId The language identifier for which the EPG data is fetched.
     * @param site The site for which the EPG data is retrieved.
     * @return Iterable collection of EpgDataDTO representing EPG data for the specified language and site.
     */
    private fun mapEpgDataFromGuideFile(languageId: String, site: String): Iterable<EpgDataDTO> =
        File(epgGrabbingConfig.getFinalOutputGuidesPath(languageId, site)).takeIf { it.exists() }?.let { guideFile ->
            xmlMapper.readValue(guideFile, EpgDTO::class.java).let { guideEpg ->
                guideEpg.programmes.orEmpty().map {
                    EpgDataDTO(
                        channelId = it.channelId,
                        title = it.title,
                        start = it.start,
                        stop = it.stop,
                        date = guideEpg.date,
                        category = it.category,
                        site = site,
                        lang = languageId
                    )
                }
            }
        }.orEmpty()

    /**
     * Fetches Electronic Program Guide (EPG) results for a specific language and site.
     *
     * @param languageId The language identifier for which the EPG data is fetched.
     * @param site The site for which the EPG data is retrieved.
     * @return A Pair object containing the site and the EPG grabber status (success or failure).
     *         In case the output guide file does not exist, it triggers EPG grabbing asynchronously.
     */
    private fun fetchEpgResults(languageId: String, site: String) = if (!File(epgGrabbingConfig.getFinalOutputGuidesPath(languageId, site)).exists()) {
        log.debug("fetchEpgForSites - Output guide file does not exist for site: $site. Running EPG Grabber.")
        runEpgGrabberAsync(site).get(EPG_GRABBER_MAX_TIMEOUT_MINUTES, TimeUnit.MINUTES)
    } else {
        log.debug("fetchEpgForSites - Output guide file exists for site: $site. Skipping EPG Grabber.")
        Pair(site, EPG_GRABBER_SUCCESS)
    }

    /**
     * Asynchronously runs the EPG grabber tool for a specified site.
     *
     * @param site The site name for which the EPG grabber tool is executed.
     * @return An integer code indicating the result of the EPG grabber tool execution.
     */
    private fun runEpgGrabberAsync(site: String): CompletableFuture<Pair<String, Int>> =
        CompletableFuture.supplyAsync {
            val process = with(epgGrabbingConfig) {
                ProcessBuilder(
                    npmCmd,
                    "epg-grabber",
                    "--config=${jsConfigPath.replace("{site}", site)}",
                    "--channels=${channelsPath.replace("{site}", site)}",
                    "--output=$outputGuidesPath"
                )
                    .directory(File(sitesBaseFolder))
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start()
            }
            val exitCode = process.waitFor()
            Pair(site, exitCode)
        }

    /**
     * Checks if the operating system is Windows.
     *
     * @return `true` if the operating system is Windows, `false` otherwise.
     */
    private fun isWindows(): Boolean =
        System.getProperty("os.name")
            .lowercase(Locale.getDefault())
            .contains("win")

    /**
     * The command to run npm or npx based on the operating system.
     */
    private val npmCmd = if (isWindows()) "npx.cmd" else "npx"
}