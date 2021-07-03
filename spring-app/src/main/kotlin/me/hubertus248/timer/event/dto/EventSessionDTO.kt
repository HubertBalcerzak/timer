package me.hubertus248.timer.event.dto

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.event.model.EventSession

@Konvert(from = [EventSession::class])
class EventSessionDTO(
    val events: List<EventDTO>
)