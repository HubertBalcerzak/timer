package me.hubertus248.timer.event.dto

import java.time.LocalDate

data class StartEventDTO(
    val taskId: Long,
    val day: LocalDate
)