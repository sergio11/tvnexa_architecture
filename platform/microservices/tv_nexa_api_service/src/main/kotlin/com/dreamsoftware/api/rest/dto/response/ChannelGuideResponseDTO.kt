package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelGuideResponseDTO(
    /**
     * The code of the channel stream.
     */
    @SerialName("code")
    val code: String,

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
     * The language of the channel guide.
     */
    @SerialName("lang")
    val lang: String
)