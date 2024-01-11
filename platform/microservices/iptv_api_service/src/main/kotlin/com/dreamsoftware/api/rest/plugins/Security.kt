package com.dreamsoftware.api.rest.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.utils.getStringProperty
import io.ktor.server.application.*
import io.ktor.server.plugins.*

const val JWT_AUTHENTICATION = "jwt_auth"

fun Application.configureSecurity() {
    with(environment.config) {
        install(Authentication) {
            jwt(JWT_AUTHENTICATION) {
                challenge { _, _ ->
                    throw call.request.headers["Authorization"]?.let {
                        if (it.isNotEmpty()) {
                            AppException.JwtAuthenticationException("Token Expired")
                        } else {
                            BadRequestException("Authorization header can not be blank!")
                        }
                    } ?: BadRequestException("Authorization header can not be blank!")
                }
                val jwtAudience = getStringProperty("jwt.audience")
                realm = getStringProperty("jwt.realm")
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(getStringProperty("jwt.secret")))
                        .withAudience(jwtAudience)
                        .withIssuer(getStringProperty("jwt.domain"))
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(jwtAudience)) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
            }
        }
    }
}
