package com.dreamsoftware.data.epg.model

import java.util.*

/**
 * Data class representing Electronic Program Guide (EPG) data for a program within a channel.
 *
 * @property channelId The unique identifier of the channel to which the program belongs.
 * @property title The title or name of the program.
 * @property start The start date and time of the program.
 * @property stop The end date and time of the program.
 * @property date The date when the program's EPG data is available.
 * @property category The category or genre of the program (optional).
 * @property site The site for which EPG data is retrieved.
 * @property lang The language for which EPG data is retrieved.
 */
data class EpgDataDTO(
    val channelId: String? = null,
    val title: String,
    val start: Date,
    val stop: Date,
    val date: Date,
    val category: String? = null,
    val site: String,
    val lang: String
)
