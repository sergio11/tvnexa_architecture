package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.util.*

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
