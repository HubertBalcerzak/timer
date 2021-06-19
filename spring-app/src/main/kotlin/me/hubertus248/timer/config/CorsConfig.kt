package me.hubertus248.timer.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import org.koin.ktor.ext.inject

fun Application.configureCors() {
    val environmentProperties: EnvironmentProperties by inject()

    if (environmentProperties.cors) {
        install(CORS) {
            anyHost()
            allowCredentials = true
            header(HttpHeaders.Authorization)
            header(HttpHeaders.ContentType)
        }
    }
}
