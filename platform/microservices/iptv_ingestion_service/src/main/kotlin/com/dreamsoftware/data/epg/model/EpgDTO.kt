package com.dreamsoftware.data.epg.model


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
