package me.hubertus248.timer.task

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.keycloakPrincipal
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.task.dto.CreateTaskDTO
import me.hubertus248.timer.task.dto.TaskDTO
import me.hubertus248.timer.task.dto.konvert
import me.hubertus248.timer.task.model.CreateTask
import me.hubertus248.timer.task.model.konvert
import me.hubertus248.timer.task.service.TaskService

fun Route.taskRouting(taskService: TaskService) {
    authenticate("keycloak") {
        route("/api/tasks") {
            get {
                val pageable = Pageable.receive(call)
                val query = call.parameters["query"]
                taskService.getTasks(pageable, query, keycloakPrincipal)
                    .let { call.respond(it.map { el -> el.konvert(TaskDTO::class) }) }
            }

            get("{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()
                taskService.getTask(id, keycloakPrincipal)
                    .let { call.respond(it.konvert(TaskDTO::class)) }
            }

            post {
                taskService.createTask(call.receive<CreateTaskDTO>().konvert(CreateTask::class), keycloakPrincipal)
                    .let { call.respond(it.konvert(TaskDTO::class)) }
            }

            put("{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()
                taskService.updateTask(id, call.receive<CreateTaskDTO>().konvert(CreateTask::class), keycloakPrincipal)
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()

                taskService.deleteTask(id, keycloakPrincipal)
            }
        }
    }
}
