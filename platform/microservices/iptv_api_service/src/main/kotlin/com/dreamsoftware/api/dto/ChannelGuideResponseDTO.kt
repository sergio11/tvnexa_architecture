package com.dreamsoftware.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelGuideResponseDTO(
    /**
     * The unique identifier of the channel guide entry.
     */
    @SerialName("id")
    val id: Long,

    /**
     * The site or source of the channel guide information.
     */
    @SerialName("site")
    val site: String,

    /**
     * The channel information associated with the guide entry.
     */
    @SerialName("channel")
    val channel: ChannelResponseDTO,

    /**
     * The language of the channel guide.
     */
    @SerialName("lang")
    val lang: String,

    /**
     * The number of days for which the guide is available.
     */
    @SerialName("days")
    val days: Int
)