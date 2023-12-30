package com.dreamsoftware.api.plugins

import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    install(ConditionalHeaders)
    installCompression()
    installCachingHeaders()
}

private fun Application.installCompression() {
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }
}

private fun Application.installCachingHeaders() {
    install(CachingHeaders) {
        options { call, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> setCSSCachingOptions()
                else -> null
            }
        }
    }
}

private fun setCSSCachingOptions() = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))