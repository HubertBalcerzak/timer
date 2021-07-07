package me.hubertus248.timer.event.dto

import java.time.Instant

data class UpdateEventDTO(
    val start: Instant?,
    val end: Instant?
)