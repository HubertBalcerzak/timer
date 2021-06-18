package me.hubertus248.timer.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.configureCors() {
    install(CORS) {//TODO add configuration
        anyHost()
        allowCredentials = true
        header(HttpHeaders.Authorization)
        header(HttpHeaders.ContentType)
    }
}
