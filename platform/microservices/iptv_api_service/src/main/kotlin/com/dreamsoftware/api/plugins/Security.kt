package com.dreamsoftware.api.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

fun Application.configureSecurity() {
    authentication {
            jwt {
                with(this@configureSecurity.environment.config) {
                    val jwtAudience = property("jwt.audience").getString()
                    realm = property("jwt.realm").getString()
                    verifier(
                        JWT
                            .require(Algorithm.HMAC256("secret"))
                            .withAudience(jwtAudience)
                            .withIssuer(property("jwt.domain").getString())
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
