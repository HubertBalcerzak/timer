package me.hubertus248.timer.event.model

import java.time.Instant
import java.time.LocalDate

data class Event(
    val id: Long,
    val day: LocalDate,
    val start: Instant,
    val end: Instant?,
    val taskId: Long,
    val taskName: String
)