package me.hubertus248.timer.event.service

import me.hubertus248.timer.event.mapper.SessionMapper
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

interface SessionService {

    fun getOrCreateSession(day: LocalDate, userId: Long): Long
}

class SessionServiceImpl : SessionService, KoinComponent {

    private val sessionMapper by inject<SessionMapper>()

    override fun getOrCreateSession(day: LocalDate, userId: Long): Long = transaction {
        sessionMapper.getOpenSessionId(day, userId) ?: sessionMapper.openSession(day, userId)
    }

}