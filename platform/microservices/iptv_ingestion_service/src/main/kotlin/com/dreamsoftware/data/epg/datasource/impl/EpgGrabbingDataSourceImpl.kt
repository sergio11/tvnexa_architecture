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

internal class EpgGrabbingDataSourceImpl(
    private val kotlinXmlMapper: ObjectMapper,
    private val epgGrabbingConfig: EpgGrabbingConfig
) : IEpgGrabbingDataSource {

    override suspend fun fetchEpgForSites(languageId: String, sites: Iterable<String>): Iterable<EpgDataDTO> =
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

    private fun isWindows(): Boolean =
        System.getProperty("os.name")
            .lowercase(Locale.getDefault())
            .contains("win")

    private val npmCmd = if (isWindows()) "npx.cmd" else "npx"

    companion object {
        private const val EPG_GRABBER_FAILED = 1
    }
}