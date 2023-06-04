package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

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
