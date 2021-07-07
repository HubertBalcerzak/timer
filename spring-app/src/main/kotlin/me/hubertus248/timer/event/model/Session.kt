package me.hubertus248.timer.event.model

import java.time.Instant
import java.time.LocalDate

data class Session(
    val id: Long,
    val userId: Long,
    val day: LocalDate,
    val createdAt: Instant
)