package com.dreamsoftware.api.rest.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO representing the request for PIN verification.
 *
 * @property pin The PIN to be verified.
 */
@Serializable
data class PinVerificationRequestDTO(
    @SerialName("pin")
    val pin: Int
)
