package com.dreamsoftware.core

import com.google.gson.reflect.TypeToken

class TypeConverter {
    companion object {
        inline fun <reified T> toRawType(): Class<T> = T::class.java
        inline fun <reified T> List<T>.toRawType(): Class<in List<T>>? = object : TypeToken<List<T>>() {}.rawType
    }
}