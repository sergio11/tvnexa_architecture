package com.dreamsoftware.api.rest.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDTO<T>(
    @SerialName("code")
    private val code: Int,
    @SerialName("message")
    private val message: String,
    @SerialName("data")
    private val data: T
)
