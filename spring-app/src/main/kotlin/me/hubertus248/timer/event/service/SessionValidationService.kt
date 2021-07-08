package me.hubertus248.timer.event.service

import io.ktor.util.reflect.*
import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.event.mapper.EventMapper
import me.hubertus248.timer.event.mapper.SessionMapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.time.temporal.ChronoUnit

interface SessionValidationService {

    fun checkSessionExists(sessionId: Long, userId: Long)

    fun checkSessionMergePossible(session1: Long, session2: Long)
}

class SessionValidationServiceImpl() : SessionValidationService, KoinComponent {

    private val sessionMapper by inject<SessionMapper>()

    private val eventMapper by inject<EventMapper>()

    override fun checkSessionExists(sessionId: Long, userId: Long) {
        if (!sessionMapper.isSessionExists(sessionId, userId)) {
            throw NotFoundException()
        }
    }

    override fun checkSessionMergePossible(session1: Long, session2: Long) {
        val session1Event = eventMapper.getLastSessionEvent(session1)
        val session2Event = eventMapper.getFirstSessionEvent(session2)
        if (session2Event.start.truncatedTo(ChronoUnit.MINUTES) != session1Event.end?.truncatedTo(ChronoUnit.MINUTES)) {
            throw BadRequestException()
        }
    }

}