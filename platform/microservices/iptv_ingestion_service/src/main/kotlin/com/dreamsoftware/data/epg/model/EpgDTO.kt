package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.util.*

@JacksonXmlRootElement(localName = "tv")
internal data class EpgDTO(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @field:JacksonXmlProperty(localName = "date", isAttribute = true)
    val date: Date,
    @field:JacksonXmlProperty(localName = "channel")
    val channels: List<EpgChannelDTO>,
    @field:JacksonXmlProperty(localName = "programme")
    val programmes: List<EpgChannelProgrammeDTO>
)
