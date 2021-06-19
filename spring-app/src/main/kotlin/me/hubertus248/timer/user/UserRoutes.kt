package me.hubertus248.timer.user

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.keycloakPrincipal
import me.hubertus248.timer.user.service.UserService
import org.koin.ktor.ext.inject

fun Route.userRouting() {

    val userService: UserService by inject()

    authenticate("keycloak") {
        route("/api/users") {
            post {
                userService.createIfNotExists(keycloakPrincipal)
                    .let { created ->
                        call.respond(if (created) HttpStatusCode.Created else HttpStatusCode.OK)
                    }
            }
        }
    }
}
