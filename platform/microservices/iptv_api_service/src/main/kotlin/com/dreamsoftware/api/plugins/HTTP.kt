package com.dreamsoftware.api.plugins

import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    install(ConditionalHeaders)
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }
    install(CachingHeaders) {
        options { call, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
                else -> null
            }
        }
    }
}
