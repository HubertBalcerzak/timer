package me.hubertus248.timer.event.service

import me.hubertus248.timer.event.mapper.SessionMapper
import me.hubertus248.timer.event.model.Session
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.time.LocalDate

interface SessionService {

    fun getOrCreateSession(day: LocalDate, userId: Long): Long

    fun getEventSession(eventId: Long): Session

    fun getDaySessions(day: LocalDate, userId: Long): List<Session>

    fun updateSession(sessionId: Long, startDateTime: Instant)
}

class SessionServiceImpl : SessionService, KoinComponent {

    private val sessionMapper by inject<SessionMapper>()

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


}