package me.hubertus248.timer.common

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.util.pipeline.*
import me.hubertus248.timer.common.exception.UnauthorizedException
import me.hubertus248.timer.security.KeycloakPrincipal


inline val PipelineContext<*, ApplicationCall>.keycloakPrincipal: KeycloakPrincipal
    get() =
        call.principal() ?: throw UnauthorizedException()
