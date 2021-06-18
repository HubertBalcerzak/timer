package me.hubertus248.timer.task

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.task.dto.CreateTaskDTO
import me.hubertus248.timer.task.dto.TaskDTO
import me.hubertus248.timer.task.dto.konvert
import me.hubertus248.timer.task.model.CreateTask
import me.hubertus248.timer.task.model.konvert
import me.hubertus248.timer.task.service.TaskService

fun Route.taskRouting(taskService: TaskService) {
    route("/api/tasks") {
        get {
            val pageable = Pageable.receive(call)
            val query = call.parameters["query"]
            call.respond(taskService.getTasks(pageable, query).map { it.konvert(TaskDTO::class) })
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()
            call.respond(taskService.getTask(id).konvert(TaskDTO::class))
        }

        post {
            taskService.createTask(call.receive<CreateTaskDTO>().konvert(CreateTask::class))
                .let { call.respond(it.konvert(TaskDTO::class)) }
        }

        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()
            taskService.updateTask(id, call.receive<CreateTaskDTO>().konvert(CreateTask::class))
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()

            taskService.deleteTask(id)
        }
    }
}
