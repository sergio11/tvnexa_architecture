package com.dreamsoftware.data.epg.datasource.impl

import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.data.epg.model.EpgDTO
import com.dreamsoftware.data.epg.model.EpgDataDTO
import com.dreamsoftware.model.EpgGrabbingConfig
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

/**
 * Implementation of [IEpgGrabbingDataSource] for fetching Electronic Program Guide (EPG) data.
 * This class retrieves EPG data for specified sites and language using an external EPG grabber tool.
 *
 * @param kotlinXmlMapper An instance of ObjectMapper for handling XML data.
 * @param epgGrabbingConfig Configuration settings for EPG grabbing.
 */
internal class EpgGrabbingDataSourceImpl(
    private val kotlinXmlMapper: ObjectMapper,
    private val epgGrabbingConfig: EpgGrabbingConfig
) : IEpgGrabbingDataSource {

    /**
     * Fetches EPG data for specified sites and language.
     *
     * @param languageId The language identifier for which EPG data is to be fetched.
     * @param sites An iterable collection of site names for which EPG data is retrieved.
     * @return An iterable collection of [EpgDataDTO] objects representing the fetched EPG data.
     */
    override suspend fun fetchEpgForSites(languageId: String, sites: Iterable<String>): Iterable<EpgDataDTO> =
        // Coroutine context switched to IO dispatcher for IO-bound operations
        withContext(Dispatchers.IO) {
            sites.map { async { Pair(it, runEpgGrabberAsync(it)) } }.awaitAll()
                .filter { it.second != EPG_GRABBER_FAILED }
                .let { grabberResults ->
                    with(grabberResults) {
                        if (isEmpty()) {
                            throw RuntimeException("EPG Grabber FAILED for language $languageId")
                        } else {
                            map {
                                with(epgGrabbingConfig) {
                                    sitesBaseFolder + File.separator + outputGuidesPath.replace("{lang}", languageId)
                                        .replace("{site}", it.first)
                                }
                            }
                        }
                    }
                }.map { guidePath ->
                    kotlinXmlMapper.readValue(File(guidePath), EpgDTO::class.java)
                }.fold(listOf()) { items, guideEpg ->
                    guideEpg.programmes.filterNot { programme ->
                        items.any {
                            it.channelId == programme.channelId &&
                                    programme.start.after(it.start) &&
                                    programme.stop.before(it.stop)
                        }
                    }.map {
                        EpgDataDTO(
                            channelId = it.channelId,
                            title = it.title,
                            start = it.start,
                            stop = it.stop,
                            date = guideEpg.date,
                            category = it.category
                        )
                    }.let {
                        items + it
                    }
                }
        }

    /**
     * Asynchronously runs the EPG grabber tool for a specified site.
     *
     * @param site The site name for which the EPG grabber tool is executed.
     * @return An integer code indicating the result of the EPG grabber tool execution.
     */
    private suspend fun runEpgGrabberAsync(site: String) = withContext(Dispatchers.IO) {
        with(epgGrabbingConfig) {
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
                .waitFor()
        }
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

    /**
     * Constant representing a failed result code for the EPG grabber tool.
     */
    companion object {
        private const val EPG_GRABBER_FAILED = 1
    }
}