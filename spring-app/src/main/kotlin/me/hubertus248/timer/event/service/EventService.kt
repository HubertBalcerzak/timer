package me.hubertus248.timer.event.service

import me.hubertus248.timer.event.mapper.EventMapper
import me.hubertus248.timer.event.model.DayEvents
import me.hubertus248.timer.event.model.EventSession
import me.hubertus248.timer.event.model.StartEvent
import me.hubertus248.timer.event.model.UpdateEvent
import me.hubertus248.timer.security.KeycloakPrincipal
import me.hubertus248.timer.task.service.TaskValidationService
import me.hubertus248.timer.user.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.util.Comparator

interface EventService {

    fun getEvents(date: LocalDate, principal: KeycloakPrincipal): DayEvents

    fun startEvent(startEvent: StartEvent, principal: KeycloakPrincipal): Long

    fun closeOpenEvent(principal: KeycloakPrincipal, timestamp: Instant)

    fun updateEvent(eventId: Long, updateEvent: UpdateEvent, principal: KeycloakPrincipal)
}

class EventServiceImpl : EventService, KoinComponent {

    private val eventMapper by inject<EventMapper>()
    private val eventValidationService by inject<EventValidationService>()
    private val taskValidationService by inject<TaskValidationService>()
    private val userService by inject<UserService>()
    private val sessionService by inject<SessionService>()

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getEvents(date: LocalDate, principal: KeycloakPrincipal): DayEvents = transaction {
        sessionService.getDaySessions(date, userService.getUserId(principal))
            .map { session -> EventSession(eventMapper.getEventsInSession(session.id)) }
            .let { DayEvents(it) }
    }

    override fun startEvent(startEvent: StartEvent, principal: KeycloakPrincipal) = transaction {
        val userId = userService.getUserId(principal)
        taskValidationService.checkTaskExists(startEvent.taskId, userId)

        var sessionId: Long? = null
        if (eventMapper.isEventOpen(userId)) {
            sessionId = eventMapper.getOpenEventSessionId(userId)
            closeOpenEvent(principal, Instant.now())
        }
        eventMapper.startEvent(
            startEvent.day,
            Instant.now(),
            startEvent.taskId,
            sessionId ?: sessionService.getOrCreateSession(startEvent.day, userId)
        )
            .also { log.info("Event for task ${startEvent.taskId} started. ID: $it") }
    }

    override fun closeOpenEvent(principal: KeycloakPrincipal, timestamp: Instant) = transaction {
        eventMapper.endEvent(userService.getUserId(principal), timestamp)
        log.info("Event for user ${principal.preferredUsername} closed at $timestamp")
    }

    override fun updateEvent(eventId: Long, updateEvent: UpdateEvent, principal: KeycloakPrincipal): Unit =
        transaction {
            eventValidationService.checkEventExists(eventId, userService.getUserId(principal))

            val event = eventMapper.getEvent(eventId)
            val session = sessionService.getEventSession(eventId)
            val events = eventMapper.getEventsInSession(session.id)
            val index = events.indexOf(event)

            if (updateEvent.start != null) {
                eventValidationService.checkEventStartTime(updateEvent.start, event)
                eventMapper.updateEvent(event.copy(start = updateEvent.start))
                if (index > 0) {
                    val previousEvent = events[index - 1]
                    eventValidationService.checkEventEndTime(updateEvent.start, previousEvent)
                    eventMapper.updateEvent(previousEvent.copy(end = updateEvent.start))
                    log.info("Updated previous event ${previousEvent.id}")
                }
                if (index == 0) {
                    sessionService.updateSession(session.id, updateEvent.start)
                }
            }
            if (updateEvent.end != null) {
                eventValidationService.checkEventEndTime(updateEvent.end, event)
                eventMapper.updateEvent(event.copy(end = updateEvent.end))
                if (index < events.size - 1) {
                    val nextEvent = events[index + 1]
                    eventValidationService.checkEventStartTime(updateEvent.end, nextEvent)
                    eventMapper.updateEvent(nextEvent.copy(start = updateEvent.end))
                    log.info("Updated next event ${nextEvent.id}")
                }
            }
            log.info("Updated event $eventId")
        }
}