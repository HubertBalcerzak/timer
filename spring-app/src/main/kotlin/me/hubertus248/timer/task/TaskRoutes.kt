package me.hubertus248.timer.task

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.keycloakPrincipal
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.task.dto.*
import me.hubertus248.timer.task.model.AddDayTask
import me.hubertus248.timer.task.model.CreateTask
import me.hubertus248.timer.task.model.konvert
import me.hubertus248.timer.task.service.TaskService
import java.time.ZonedDateTime

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

            get("day") {
                val date = ZonedDateTime.parse(call.parameters["date"]).toLocalDate() ?: throw BadRequestException()
                taskService.getDayTasks(date, keycloakPrincipal)
                    .map { it.konvert(DayTaskDTO::class) }
                    .let { call.respond(it) }
            }

            post {
                taskService.createTask(call.receive<CreateTaskDTO>().konvert(CreateTask::class), keycloakPrincipal)
                    .let { call.respond(it.konvert(TaskDTO::class)) }
            }

            put("{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()
                taskService.updateTask(id, call.receive<CreateTaskDTO>().konvert(CreateTask::class), keycloakPrincipal)
                call.respond(HttpStatusCode.OK)
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()

                taskService.deleteTask(id, keycloakPrincipal)
                call.respond(HttpStatusCode.OK)
            }

            post("{id}/day") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequestException()
                val addDayTaskDTO = call.receive<AddDayTaskDTO>()

                taskService.addTask(id, addDayTaskDTO.konvert(AddDayTask::class), keycloakPrincipal)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
