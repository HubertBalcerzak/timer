package me.hubertus248.timer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.dto.ErrorDTO
import me.hubertus248.timer.config.configureCors
import me.hubertus248.timer.config.configureDatabase
import me.hubertus248.timer.config.configureExceptionHandler
import me.hubertus248.timer.koin.configureKoin
import me.hubertus248.timer.security.keycloak
import me.hubertus248.timer.task.service.TaskService
import me.hubertus248.timer.task.taskRouting
import me.hubertus248.timer.user.userRouting
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module(testing: Boolean = false) {

    configureKoin()
    configureDatabase()
    configureExceptionHandler()
    configureCors()

    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
        }
    }
    install(CallLogging) {
        level = Level.DEBUG
    }

    install(Authentication) {
        keycloak("keycloak") {
            keycloakUrl = "https://keycloak.snet.ovh"
            realm = "Test"
            onAuthFailure = { context, _ ->
                context.call.respond(HttpStatusCode.Unauthorized, ErrorDTO("Unauthorized"))
            }
        }
    }

    val taskService: TaskService by inject()

    routing {
        taskRouting(taskService)
        userRouting()
    }
}
