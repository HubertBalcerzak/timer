package me.hubertus248.timer.event.mapper

import me.hubertus248.timer.event.mapper.tables.Events
import me.hubertus248.timer.event.mapper.tables.Sessions
import me.hubertus248.timer.event.model.Session
import me.hubertus248.timer.task.mapper.tables.Tasks
import org.jetbrains.exposed.sql.*
import java.time.Instant
import java.time.LocalDate

class SessionMapper {

    fun getOpenSessionId(day: LocalDate, userId: Long): Long? =
        Sessions.innerJoin(Events)
            .slice(Sessions.id)
            .select { (Events.day eq day) and (Sessions.userId eq userId) and (Events.end eq null) }
            .firstOrNull()
            ?.let { it[Sessions.id].value }

    fun openSession(day: LocalDate, userId: Long, startDateTime: Instant = Instant.now()): Long =
        Sessions.insertAndGetId {
            it[Sessions.day] = day
            it[Sessions.userId] = userId
            it[Sessions.createdAt] = startDateTime
        }.value

    fun getEventSession(eventId: Long): Session =
        Sessions.innerJoin(Events)
            .select { Events.id eq eventId }
            .first()
            .asSession()

    fun getDaySessions(day: LocalDate, userId: Long): List<Session> =
        Sessions.select { (Sessions.day eq day) and (Sessions.userId eq userId) }
            .orderBy(Sessions.createdAt)
            .map { it.asSession() }


    fun updateSession(sessionId: Long, startDateTime: Instant) =
        Sessions.update({ Sessions.id eq sessionId }) {
            it[Sessions.createdAt] = startDateTime
        }

    fun isSessionExists(sessionId: Long, userId: Long): Boolean =
        Sessions.select { (Sessions.id eq sessionId) and (Sessions.userId eq userId) }
            .count() > 0

    private fun ResultRow.asSession(): Session = Session(
        this[Sessions.id].value,
        this[Sessions.userId],
        this[Sessions.day],
        this[Sessions.createdAt]
    )

}