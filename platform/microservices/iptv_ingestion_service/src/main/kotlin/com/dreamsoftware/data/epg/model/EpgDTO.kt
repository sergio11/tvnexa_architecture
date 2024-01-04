package com.dreamsoftware.data.epg.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.util.*

/**
 * Data class representing Electronic Program Guide (EPG) data for TV channels and programs.
 *
 * @property date The date for which the EPG data is available.
 * @property channels A list of TV channels with their information.
 * @property programmes A list of programs scheduled for TV channels.
 */
@JacksonXmlRootElement(localName = "tv")
internal data class EpgDTO(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @field:JacksonXmlProperty(localName = "date", isAttribute = true)
    val date: Date,
    @field:JacksonXmlProperty(localName = "channel")
    val channels: List<EpgChannelDTO>,
    @field:JacksonXmlProperty(localName = "programme")
    val programmes: List<EpgChannelProgrammeDTO>?
)