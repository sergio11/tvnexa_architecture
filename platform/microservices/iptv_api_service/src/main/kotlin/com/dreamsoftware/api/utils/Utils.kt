package com.dreamsoftware.api.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converts the object of type [T] to its JSON representation.
 *
 * @return A JSON string representing the object of type [T].
 */
inline fun <reified T> T.toJSON(): String = Gson().toJson(this, T::class.java)

/**
 * Parses the JSON string to an object of type [T].
 *
 * @return An object of type [T] parsed from the JSON string.
 */
inline fun <reified T> String.fromJson(): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson(this, type)
}