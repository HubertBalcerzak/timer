package me.hubertus248.timer.task.dto

import com.fasterxml.jackson.annotation.JsonFormat
import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.task.model.DayTask
import java.time.Duration

@Konvert(from = [DayTask::class])
data class DayTaskDTO(
    val id: Long,
    val name: String,
    val running: Boolean,
    val duration: Duration
)