package com.dreamsoftware.api.rest.plugins

import com.dreamsoftware.api.rest.validation.configureSignUpRequestValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        configureSignUpRequestValidation()
    }
}