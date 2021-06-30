package me.hubertus248.timer.task.dto

import kotlinx.serialization.Serializable
import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.task.model.Task

@Konvert(from = [Task::class])
data class TaskDTO(
    val id: Long,
    val name: String
)
