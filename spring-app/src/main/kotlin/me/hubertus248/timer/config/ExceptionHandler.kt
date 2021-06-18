package me.hubertus248.timer.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import me.hubertus248.timer.common.exception.ApplicationException

fun Application.configureExceptionHandler() {
    install(StatusPages) {
        exception(ApplicationException::class.java) { exception ->
            log.debug("Exception: ", exception)
            call.respond(exception.statusCode, exception.errorMessage)
        }
        exception(Exception::class.java) { exception ->
            log.error("Internal server error", exception)
            call.respond(HttpStatusCode.InternalServerError, "Internal server error")
        }
    }
}
