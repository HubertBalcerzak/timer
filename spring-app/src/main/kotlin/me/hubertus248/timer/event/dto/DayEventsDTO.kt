package me.hubertus248.timer.event.dto

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.event.model.DayEvents

@Konvert(from = [DayEvents::class])
data class DayEventsDTO(
    val sessions: List<EventSessionDTO>
)