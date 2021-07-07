package me.hubertus248.timer.event.mapper

import me.hubertus248.timer.event.mapper.tables.Events
import me.hubertus248.timer.event.mapper.tables.Events.end
import me.hubertus248.timer.event.mapper.tables.Events.start
import me.hubertus248.timer.event.model.Event
import me.hubertus248.timer.task.mapper.tables.Tasks
import org.jetbrains.exposed.sql.*
import java.time.Duration
import java.time.Instant
import java.time.LocalDate


class EventMapper {

    fun getEvents(day: LocalDate, userId: Long) =
        Events.innerJoin(Tasks).select { (Events.day eq day) and (Tasks.userId eq userId) }
            .orderBy(start)
            .map { it.asEvent() }


    fun startEvent(day: LocalDate, timestamp: Instant, taskId: Long, sessionId: Long): Long =
        Events.insertAndGetId {
            it[Events.taskId] = taskId
            it[start] = timestamp
            it[Events.day] = day
            it[Events.sessionId] = sessionId
        }.value


//    fun endEvent(eventId: Long, timestamp: Instant) {
//        Events.update({ Events.id eq eventId }) {
//            it[end] = timestamp
//        }
//    }

    fun endEvent(userId: Long, timestamp: Instant) =
        Events.innerJoin(Tasks)
            .update({ (Tasks.userId eq userId) and (end eq null) }) {
                it[end] = timestamp
            }


    fun isEventOpen(userId: Long) =
        Events.innerJoin(Tasks)
            .select { (Tasks.userId eq userId) and (end eq null) }
            .count() > 0

    fun isEventForTaskOpen(taskId: Long, day: LocalDate): Boolean =
        Events.select { (Events.taskId eq taskId) and (Events.day eq day) and (end eq null) }
            .count() > 0

    fun getTaskDuration(taskId: Long, day: LocalDate): Duration =
        Events.slice(start, end)
            .select { (Events.taskId eq taskId) and (Events.day eq day) }
            .map { Duration.between(it[start], it[end] ?: Instant.now()) }
            .reduceOrNull { duration1, duration2 -> duration1.plus(duration2) } ?: Duration.ZERO

    fun getOpenEventSessionId(userId: Long): Long? =
        Events.innerJoin(Tasks)
            .slice(Events.sessionId)
            .select { (end eq null) and (Tasks.userId eq userId) }
            .firstOrNull()
            ?.let { it[Events.sessionId] }

    fun getEventExists(eventId: Long, userId: Long): Boolean =
        Events.innerJoin(Tasks)
            .select { (Events.id eq eventId) and (Tasks.userId eq userId) }
            .count() > 0

    fun getEventsInSession(sessionId: Long): List<Event> =
        Events.innerJoin(Tasks)
            .select { Events.sessionId eq sessionId }
            .orderBy(start)
            .map { it.asEvent() }

    fun getEvent(eventId: Long): Event =
        Events.innerJoin(Tasks)
            .select { Events.id eq eventId }
            .first()
            .asEvent()

    fun updateEvent(event: Event) =
        Events.update({ Events.id eq event.id }) {
            it[start] = event.start
            it[end] = event.end
        }


    private fun ResultRow.asEvent(): Event = Event(
        this[Events.id].value,
        this[Events.day],
        this[start],
        this[end],
        this[Tasks.id].value,
        this[Tasks.name],
        this[Events.sessionId]
    )
}