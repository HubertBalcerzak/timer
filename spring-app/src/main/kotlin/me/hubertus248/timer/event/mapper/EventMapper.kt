package me.hubertus248.timer.event.mapper

import me.hubertus248.timer.event.mapper.tables.Events
import me.hubertus248.timer.event.model.Event
import me.hubertus248.timer.task.mapper.tables.Tasks
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDate


class EventMapper {

    fun getEvents(day: LocalDate, userId: Long) =
        Events.innerJoin(Tasks).select { (Events.day eq day) and (Tasks.userId eq userId) }
            .orderBy(Events.start)
            .map {
                Event(
                    it[Events.id].value,
                    it[Events.day],
                    it[Events.start],
                    it[Events.end],
                    it[Tasks.id].value,
                    it[Tasks.name]
                )
            }


    fun startEvent(day: LocalDate, timestamp: Instant, taskId: Long): Long =
        Events.insertAndGetId {
            it[Events.taskId] = taskId
            it[start] = timestamp
            it[Events.day] = day
        }.value


//    fun endEvent(eventId: Long, timestamp: Instant) {
//        Events.update({ Events.id eq eventId }) {
//            it[end] = timestamp
//        }
//    }

    fun endEvent(userId: Long, timestamp: Instant) =
        Events.innerJoin(Tasks)
            .update({ (Tasks.userId eq userId) and (Events.end eq null) }) {
                it[Events.end] = timestamp
            }


    fun isEventOpen(userId: Long) =
        Events.innerJoin(Tasks)
            .select { (Tasks.userId eq userId) and (Events.end eq null) }
            .count() > 0

}