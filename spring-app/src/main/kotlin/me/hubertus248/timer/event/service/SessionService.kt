package me.hubertus248.timer.event.service

import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.event.mapper.EventMapper
import me.hubertus248.timer.event.mapper.SessionMapper
import me.hubertus248.timer.event.model.Event
import me.hubertus248.timer.event.model.Session
import me.hubertus248.timer.security.KeycloakPrincipal
import me.hubertus248.timer.user.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate

interface SessionService {

    fun getOrCreateSession(day: LocalDate, userId: Long): Long

    fun getEventSession(eventId: Long): Session

    fun getDaySessions(day: LocalDate, userId: Long): List<Session>

    fun updateSession(sessionId: Long, startDateTime: Instant)

    fun splitSession(eventId: Long, principal: KeycloakPrincipal)

    fun mergeSessions(sessionId: Long, principal: KeycloakPrincipal)
}

class SessionServiceImpl : SessionService, KoinComponent {

    private val sessionMapper by inject<SessionMapper>()
    private val eventMapper by inject<EventMapper>()
    private val eventValidationService by inject<EventValidationService>()
    private val userService by inject<UserService>()
    private val sessionValidationService by inject<SessionValidationService>()
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getOrCreateSession(day: LocalDate, userId: Long): Long = transaction {
        sessionMapper.getOpenSessionId(day, userId) ?: sessionMapper.openSession(day, userId)
    }

    override fun getEventSession(eventId: Long): Session {
        return sessionMapper.getEventSession(eventId)
    }

    override fun getDaySessions(day: LocalDate, userId: Long): List<Session> {
        return sessionMapper.getDaySessions(day, userId)
    }

    override fun updateSession(sessionId: Long, startDateTime: Instant) {
        sessionMapper.updateSession(sessionId, startDateTime)
    }

    override fun splitSession(eventId: Long, principal: KeycloakPrincipal) = transaction {
        val userId = userService.getUserId(principal)
        eventValidationService.checkEventExists(eventId, userId)
        val session = sessionMapper.getEventSession(eventId)
        val events = eventMapper.getEventsInSession(session.id)
        val lastEvent = eventMapper.getEvent(eventId)
        val index = events.indexOf(lastEvent)

        if (index == events.size - 1) {
            throw BadRequestException()
        }
        val eventsToMove = events.subList(index + 1, events.size)

        val newSessionId = sessionMapper.openSession(lastEvent.day, userId, eventsToMove.first().start)
        eventsToMove.forEach {
            eventMapper.updateEvent(it.copy(sessionId = newSessionId))
        }
        log.info("Session ${session.id} split on event ${lastEvent.id}")
    }

    override fun mergeSessions(sessionId: Long, principal: KeycloakPrincipal) {
        sessionValidationService.checkSessionExists(sessionId, userService.getUserId(principal))



    }
}