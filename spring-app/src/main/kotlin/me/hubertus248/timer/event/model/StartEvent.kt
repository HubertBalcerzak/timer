package me.hubertus248.timer.event.model

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.event.dto.StartEventDTO
import java.time.LocalDate

@Konvert(from = [StartEventDTO::class])
class StartEvent(
    val taskId: Long,
    val day: LocalDate
)