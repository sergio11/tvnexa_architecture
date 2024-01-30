package com.dreamsoftware.core

import java.security.MessageDigest
import java.time.Instant
import java.time.ZoneId
import java.util.*

fun Date.toLocalDateTime() = Instant.ofEpochMilli(time)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

fun String.hash256() =
    MessageDigest.getInstance("SHA-256").digest(toByteArray())
        .joinToString("") { "%02x".format(it) }

fun String.toUUID() =
    UUID.fromString(this)