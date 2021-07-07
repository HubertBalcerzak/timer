package me.hubertus248.timer.event.service

import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.event.mapper.SessionMapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface SessionValidationService {

    fun checkSessionExists(sessionId: Long, userId: Long)

    fun checkSessionMergePossible(session1: Long, session2: Long)
}

class SessionValidationServiceImpl() : SessionValidationService, KoinComponent {

    private val sessionMapper by inject<SessionMapper>()

    override fun checkSessionExists(sessionId: Long, userId: Long) {
        if (!sessionMapper.isSessionExists(sessionId, userId)) {
            throw NotFoundException()
        }
    }

    override fun checkSessionMergePossible(session1: Long, session2: Long) {
        TODO("Not yet implemented")
    }

}