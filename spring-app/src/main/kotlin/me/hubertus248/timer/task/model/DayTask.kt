package me.hubertus248.timer.task.model

import java.time.Duration

data class DayTask(
    val id: Long,
    val name: String,
    val running: Boolean,
    val duration: Duration
)