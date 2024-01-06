package com.dreamsoftware.core

import java.time.Instant
import java.time.ZoneId
import java.util.*

fun Date.toLocalDateTime() = Instant.ofEpochMilli(time)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()