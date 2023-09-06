package com.dreamsoftware.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpgChannelProgrammeResponseDTO(
    /**
     * The unique identifier of the program.
     */
    @SerialName("id")
    val id: Long,

    /**
     * The title or name of the program.
     */
    @SerialName("title")
    val title: String,

    /**
     * The channel where the program is broadcast.
     */
    @SerialName("channel")
    val channel: ChannelResponseDTO,

    /**
     * The category or genre of the program (if available).
     */
    @SerialName("category")
    val category: CategoryResponseDTO?,

    /**
     * The start date and time of the program.
     */
    @SerialName("start")
    val start: String,

    /**
     * The end date and time of the program.
     */
    @SerialName("end")
    val end: String,

    /**
     * The date and time when the program will air.
     */
    @SerialName("date")
    val date: String
)