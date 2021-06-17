package me.hubertus248.timer.task.model

import me.hubertus248.konvert.api.Konvert
import me.hubertus248.timer.task.dto.CreateTaskDTO

@Konvert(from = [CreateTaskDTO::class])
data class CreateTask(val name: String)
