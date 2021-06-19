package me.hubertus248.timer.security

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.auth.*

class KeycloakPrincipal(
    val subject: String,
    val preferredUsername: String,
    val roles: List<Role>,
    val emailVerified: Boolean,
    val audiences: List<String>,
    val credentials: DecodedJWT
) : Principal

data class Role(val value: String)
