package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelDetailResponseDTO(
    @SerialName("channel_id")
    val channelId: String,

    /**
     * The name of the channel.
     */
    @SerialName("name")
    val name: String? = null,

    /**
     * The network to which the channel belongs.
     */
    @SerialName("network")
    val network: String? = null,

    /**
     * The country where the channel is based.
     */
    @SerialName("country")
    val country: CountryResponseDTO,

    /**
     * The subdivision or region within the country (if applicable).
     */
    @SerialName("subdivision")
    val subdivision: SubdivisionResponseDTO? = null,

    /**
     * The city or location associated with the channel (if applicable).
     */
    @SerialName("city")
    val city: String? = null,

    /**
     * Indicates whether the channel contains NSFW (Not Safe For Work) content.
     */
    @SerialName("is_nsfw")
    val isNsfw: Boolean? = null,

    /**
     * The website URL associated with the channel.
     */
    @SerialName("website")
    val website: String? = null,

    /**
     * The URL to the channel's logo or image.
     */
    @SerialName("logo")
    val logo: String? = null,

    /**
     * The date when the channel was launched or started.
     */
    @SerialName("launched")
    val launched: String? = null,

    /**
     * The date when the channel was closed (if applicable).
     */
    @SerialName("closed")
    val closed: String? = null,

    /**
     * The channel that replaced this channel (if applicable).
     */
    @SerialName("replaced_by")
    val replacedBy: ChannelDetailResponseDTO? = null,

    /**
     * The stream data for the channel
     */
    @SerialName("streams")
    val streams: List<ChannelStreamResponseDTO>,

    /**
     * Guides of the channel
     */
    @SerialName("guides")
    val guides: List<ChannelGuideResponseDTO>,

    /**
     * The languages spoken or used by the channel.
     */
    @SerialName("languages")
    val languages: List<LanguageResponseDTO>,

    /**
     * The categories or genres to which the channel belongs.
     */
    @SerialName("categories")
    val categories: List<CategoryResponseDTO>,

    /**
     * List of alternative channel names.
     */
    @SerialName("alt_names")
    val altNames: List<String>,

    /**
     * List of channel owners.
     */
    @SerialName("owners")
    val owners: List<String>,

    /**
     * List of codes describing the broadcasting area (r/<region_code>, c/<country_code>, s/<subdivision_code>)
     */
    @SerialName("broadcast_areas")
    val broadcastAreas: List<String>,
)