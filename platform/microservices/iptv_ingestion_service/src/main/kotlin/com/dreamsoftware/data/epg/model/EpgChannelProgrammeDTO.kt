package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.util.*

/**
 * Data class representing a program (programme) within an Electronic Program Guide (EPG) channel
 * retrieved from XML data.
 *
 * This class is used for deserializing XML data into Kotlin objects using Jackson XmlMapper.
 *
 * @property start The start date and time of the program.
 * @property stop The end date and time of the program.
 * @property channelId The unique identifier of the channel to which the program belongs.
 * @property title The title or name of the program.
 * @property category The category or genre of the program (optional).
 */
@JacksonXmlRootElement(localName = "programme")
internal data class EpgChannelProgrammeDTO(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss Z")
    @field:JacksonXmlProperty(localName = "start", isAttribute = true)
    val start: Date,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss Z")
    @field:JacksonXmlProperty(localName = "stop", isAttribute = true)
    val stop: Date,
    @field:JacksonXmlProperty(localName = "channel", isAttribute = true)
    val channelId: String,
    @field:JacksonXmlProperty(localName = "title")
    val title: String,
    @field:JacksonXmlProperty(localName = "category")
    val category: String? = null
)
