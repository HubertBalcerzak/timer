package me.hubertus248.timer.event.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.keycloakPrincipal
import me.hubertus248.timer.event.dto.DayEventsDTO
import me.hubertus248.timer.event.dto.StartEventDTO
import me.hubertus248.timer.event.dto.konvert
import me.hubertus248.timer.event.model.StartEvent
import me.hubertus248.timer.event.model.konvert
import me.hubertus248.timer.event.service.EventService
import me.hubertus248.timer.task.service.TaskService
import org.koin.ktor.ext.inject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

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
                val date = ZonedDateTime.parse(call.parameters["date"]).toLocalDate() ?: throw BadRequestException()
                eventService.getEvents(date, keycloakPrincipal)
                    .konvert(DayEventsDTO::class)
                    .let { call.respond(it) }
            }
        }
    }
}