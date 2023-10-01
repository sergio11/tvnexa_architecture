package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Data class representing an Electronic Program Guide (EPG) channel retrieved from XML data.
 *
 * This class is used for deserializing XML data into Kotlin objects using Jackson XmlMapper.
 *
 * @property channelId The unique identifier of the EPG channel.
 * @property displayName The display name or title of the EPG channel.
 * @property url The URL associated with the EPG channel.
 */
@JacksonXmlRootElement(localName = "channel")
internal data class EpgChannelDTO(
    @field:JacksonXmlProperty(localName = "id", isAttribute = true)
    val channelId: String,
    @field:JacksonXmlProperty(localName = "display-name")
    val displayName: String,
    @field:JacksonXmlProperty(localName = "url")
    val url: String
)
