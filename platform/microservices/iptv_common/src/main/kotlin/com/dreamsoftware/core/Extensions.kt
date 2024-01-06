package com.dreamsoftware.core

import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.time.ZoneId
import java.util.*

fun Date.toLocalDateTime() = Instant.ofEpochMilli(time)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()


inline fun <reified T> T.toRawType(): Class<T> = T::class.java
inline fun <reified T> List<T>.toRawType(): Class<in List<T>>? = object : TypeToken<List<T>>() {}.rawType