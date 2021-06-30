package me.hubertus248.timer.task.model

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.task.dto.AddDayTaskDTO
import java.time.LocalDate

@Konvert(from = [AddDayTaskDTO::class])
data class AddDayTask(val date: LocalDate)
