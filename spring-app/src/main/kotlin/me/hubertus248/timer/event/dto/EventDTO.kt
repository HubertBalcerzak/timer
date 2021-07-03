package me.hubertus248.timer.event.dto

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.event.model.Event
import java.time.Instant

@Konvert(from = [Event::class])
class EventDTO(
    val id: Long,
    val start: Instant,
    val end: Instant?,
    val taskId: Long,
    val taskName: String,
)