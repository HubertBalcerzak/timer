package me.hubertus248.timer.event.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.keycloakPrincipal
import me.hubertus248.timer.event.dto.StartEventDTO
import me.hubertus248.timer.event.model.StartEvent
import me.hubertus248.timer.event.model.konvert
import me.hubertus248.timer.event.service.EventService
import me.hubertus248.timer.task.service.TaskService
import org.koin.ktor.ext.inject
import java.time.Instant

fun Route.eventRouting() {
    val eventService by inject<EventService>()

    authenticate("keycloak") {
        route("/api/events") {
            post("/start") {
                call.receive<StartEventDTO>().konvert(StartEvent::class)
                    .let { startEvent -> eventService.startEvent(startEvent, keycloakPrincipal) }
                    .let { call.respond(HttpStatusCode.Created) }

            }
            post("/end") {
                eventService.closeOpenEvent(keycloakPrincipal, Instant.now())
                call.respond(HttpStatusCode.OK)
            }
            get {

            }
        }
    }
}