package com.dreamsoftware.data.iptvorg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a channel guide in an IPTV organization project.
 *
 * @property channel Identifier of the channel.
 * @property site Site or source of the channel guide.
 * @property siteId Unique channel ID used on the site.
 * @property siteName Channel name used on the site
 * @property lang Language of the guide (ISO 639-1 code)
 */
@Serializable
data class ChannelGuideDTO(
    @SerialName("channel")
    val channel: String?,
    @SerialName("site")
    val site: String,
    @SerialName("site_id")
    val siteId: String,
    @SerialName("site_name")
    val siteName: String,
    @SerialName("lang")
    val lang: String
)
