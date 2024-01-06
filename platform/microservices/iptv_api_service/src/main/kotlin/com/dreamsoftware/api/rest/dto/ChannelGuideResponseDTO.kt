package com.dreamsoftware.api.rest.dto

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
     * Unique channel ID used on the site.
     */
    @SerialName("site_id")
    val siteId: String,

    /**
     * Channel name used on the site
     */
    @SerialName("site_name")
    val siteName: String,

    /**
     * The channel information associated with the guide entry.
     */
    @SerialName("channel")
    val channel: SimpleChannelResponseDTO,

    /**
     * The language of the channel guide.
     */
    @SerialName("lang")
    val lang: String
)