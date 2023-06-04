package com.dreamsoftware.data.iptvorg.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "tv")
data class EpgDTO(
    @field:JacksonXmlProperty(localName = "date", isAttribute = true)
    val date: String,
    @field:JacksonXmlProperty(localName = "channel")
    val channels: List<EpgChannelDTO>,
    @field:JacksonXmlProperty(localName = "programme")
    val programmes: List<EpgChannelProgrammeDTO>
)

@JacksonXmlRootElement(localName = "channel")
data class EpgChannelDTO(
    @field:JacksonXmlProperty(localName = "id", isAttribute = true)
    val channelId: String,
    @field:JacksonXmlProperty(localName = "display-name")
    val displayName: String,
    @field:JacksonXmlProperty(localName = "url")
    val url: String
)

@JacksonXmlRootElement(localName = "programme")
data class EpgChannelProgrammeDTO(
    @field:JacksonXmlProperty(localName = "start", isAttribute = true)
    val start: String,
    @field:JacksonXmlProperty(localName = "stop", isAttribute = true)
    val stop: String,
    @field:JacksonXmlProperty(localName = "channel", isAttribute = true)
    val channelId: String,
    @field:JacksonXmlProperty(localName = "title")
    val title: String,
    @field:JacksonXmlProperty(localName = "category")
    val category: String? = null
)
