package me.hubertus248.timer.task.service

import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.common.exception.SystemException
import me.hubertus248.timer.common.pagination.Page
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.security.KeycloakPrincipal
import me.hubertus248.timer.task.mapper.TaskMapper
import me.hubertus248.timer.task.model.CreateTask
import me.hubertus248.timer.task.model.Task
import me.hubertus248.timer.user.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface TaskService {

    fun getTask(taskId: Long, principal: KeycloakPrincipal): Task

    fun getTasks(pageable: Pageable, query: String?, principal: KeycloakPrincipal): Page<Task>

    fun createTask(createTask: CreateTask, principal: KeycloakPrincipal): Task

    fun updateTask(taskId: Long, createTask: CreateTask, principal: KeycloakPrincipal): Task

    fun deleteTask(taskId: Long, principal: KeycloakPrincipal)
}

class TaskServiceImpl : TaskService, KoinComponent {

    private val taskMapper: TaskMapper by inject()
    private val taskValidationService: TaskValidationService by inject()
    private val userService: UserService by inject()

    override fun getTask(taskId: Long, principal: KeycloakPrincipal): Task = transaction {
        taskMapper.getTask(taskId, userService.getUserId(principal)) ?: throw NotFoundException()
    }

    override fun getTasks(pageable: Pageable, query: String?, principal: KeycloakPrincipal): Page<Task> = transaction {
        taskMapper.getTasks(pageable, query, userService.getUserId(principal))
    }

    override fun createTask(createTask: CreateTask, principal: KeycloakPrincipal): Task = transaction {
        val userId = userService.getUserId(principal)
        taskMapper.createTask(createTask, userId)
            .let { taskMapper.getTask(it, userId) ?: throw SystemException() }
    }

    override fun updateTask(taskId: Long, createTask: CreateTask, principal: KeycloakPrincipal): Task = transaction {
        val userId = userService.getUserId(principal)
        taskValidationService.checkTaskExists(taskId, userId)
        taskMapper.updateTask(taskId, createTask, userId)
        taskMapper.getTask(taskId, userId) ?: throw SystemException()
    }

    override fun deleteTask(taskId: Long, principal: KeycloakPrincipal) = transaction {
        val userId = userService.getUserId(principal)
        taskValidationService.checkTaskExists(taskId, userId)
        taskMapper.deleteTask(taskId, userId)
    }
}
