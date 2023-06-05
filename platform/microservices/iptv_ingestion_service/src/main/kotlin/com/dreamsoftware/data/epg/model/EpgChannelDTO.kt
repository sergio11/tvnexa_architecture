package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "channel")
internal data class EpgChannelDTO(
    @field:JacksonXmlProperty(localName = "id", isAttribute = true)
    val channelId: String,
    @field:JacksonXmlProperty(localName = "display-name")
    val displayName: String,
    @field:JacksonXmlProperty(localName = "url")
    val url: String
)
