package com.dreamsoftware.api.plugins

import com.dreamsoftware.api.rest.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        configureCommonStatusPages()
        configureCategoryStatusPages()
        configureChannelStatusPages()
        configureCountryStatusPages()
        configureRegionStatusPages()
        configureEpgChannelStatusPages()
    }
}