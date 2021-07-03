package me.hubertus248.timer.task.service

import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.common.exception.SystemException
import me.hubertus248.timer.common.pagination.Page
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.security.KeycloakPrincipal
import me.hubertus248.timer.task.mapper.TaskMapper
import me.hubertus248.timer.task.model.AddDayTask
import me.hubertus248.timer.task.model.CreateTask
import me.hubertus248.timer.task.model.DayTask
import me.hubertus248.timer.task.model.Task
import me.hubertus248.timer.user.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDate

interface TaskService {

    fun getTask(taskId: Long, principal: KeycloakPrincipal): Task

    fun getTasks(pageable: Pageable, query: String?, principal: KeycloakPrincipal): Page<Task>

    fun getDayTasks(date: LocalDate, principal: KeycloakPrincipal): List<DayTask>

    fun createTask(createTask: CreateTask, principal: KeycloakPrincipal): Task

    fun updateTask(taskId: Long, createTask: CreateTask, principal: KeycloakPrincipal): Task

    fun deleteTask(taskId: Long, principal: KeycloakPrincipal)

    fun addTask(taskId: Long, addDayTask: AddDayTask, principal: KeycloakPrincipal)
}

class TaskServiceImpl : TaskService, KoinComponent {

    private val taskMapper: TaskMapper by inject()
    private val taskValidationService: TaskValidationService by inject()
    private val userService: UserService by inject()
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getTask(taskId: Long, principal: KeycloakPrincipal): Task = transaction {
        taskMapper.getTask(taskId, userService.getUserId(principal)) ?: throw NotFoundException()
    }

    override fun getTasks(pageable: Pageable, query: String?, principal: KeycloakPrincipal): Page<Task> = transaction {
        taskMapper.getTasks(pageable, query, userService.getUserId(principal))
    }

    override fun getDayTasks(date: LocalDate, principal: KeycloakPrincipal) = transaction {
        taskMapper.getTasks(date, userService.getUserId(principal))
    }

    override fun createTask(createTask: CreateTask, principal: KeycloakPrincipal): Task = transaction {
        val userId = userService.getUserId(principal)
        taskMapper.createTask(createTask, userId)
            .let { taskMapper.getTask(it, userId) ?: throw SystemException() }
            .also { log.info("Task ${it.name} created. ID: ${it.id}") }
    }

    override fun updateTask(taskId: Long, createTask: CreateTask, principal: KeycloakPrincipal): Task = transaction {
        val userId = userService.getUserId(principal)
        taskValidationService.checkTaskExists(taskId, userId)
        taskMapper.updateTask(taskId, createTask, userId)

        log.info("Task $taskId updated")

        taskMapper.getTask(taskId, userId) ?: throw SystemException()
    }

    override fun deleteTask(taskId: Long, principal: KeycloakPrincipal) = transaction {
        val userId = userService.getUserId(principal)
        taskValidationService.checkTaskExists(taskId, userId)
        taskMapper.deleteTask(taskId, userId)
        log.info("Task $taskId deleted")
    }

    override fun addTask(taskId: Long, addDayTask: AddDayTask, principal: KeycloakPrincipal) = transaction {
        taskValidationService.checkTaskExists(taskId, userService.getUserId(principal))
        taskValidationService.checkTaskNotAdded(taskId, addDayTask.date)
        taskMapper.addDayTask(taskId, addDayTask.date)
        log.info("Task $taskId added to day ${addDayTask.date}")
    }
}
