package me.hubertus248.timer.event.model

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.event.dto.UpdateEventDTO
import java.time.Instant

@Konvert(from = [UpdateEventDTO::class])
class UpdateEvent(
    val start: Instant?,
    val end: Instant?
)