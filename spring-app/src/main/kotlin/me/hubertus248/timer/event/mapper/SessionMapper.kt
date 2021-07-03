package me.hubertus248.timer.event.mapper

import me.hubertus248.timer.event.mapper.tables.Events
import me.hubertus248.timer.event.mapper.tables.Sessions
import me.hubertus248.timer.task.mapper.tables.Tasks
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import java.time.Instant
import java.time.LocalDate

class SessionMapper {

    fun getOpenSessionId(day: LocalDate, userId: Long): Long? =
        Sessions.innerJoin(Events)
            .slice(Sessions.id)
            .select { (Events.day eq day) and (Sessions.userId eq userId) and (Events.end eq null) }
            .firstOrNull()
            ?.let { it[Sessions.id].value }

    fun openSession(day: LocalDate, userId: Long): Long =
        Sessions.insertAndGetId {
            it[Sessions.day] = day
            it[Sessions.userId] = userId
            it[Sessions.createdAt] = Instant.now()
        }.value

}