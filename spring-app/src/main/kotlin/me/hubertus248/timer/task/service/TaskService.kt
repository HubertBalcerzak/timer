package me.hubertus248.timer.task.service

import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.common.exception.SystemException
import me.hubertus248.timer.common.pagination.Page
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.task.mapper.TaskMapper
import me.hubertus248.timer.task.model.CreateTask
import me.hubertus248.timer.task.model.Task
import org.jetbrains.exposed.sql.transactions.transaction

interface TaskService {

    fun getTask(taskId: Long): Task

    fun getTasks(pageable: Pageable): Page<Task>

    fun createTask(createTask: CreateTask): Task

    fun updateTask(taskId: Long, createTask: CreateTask): Task

    fun deleteTask(taskId: Long)
}

class TaskServiceImpl(private val taskMapper: TaskMapper, private val taskValidationService: TaskValidationService) :
    TaskService {

    override fun getTask(taskId: Long): Task =
        taskMapper.getTask(taskId) ?: throw NotFoundException()

    override fun getTasks(pageable: Pageable): Page<Task> =
        taskMapper.getTasks(pageable)

    override fun createTask(createTask: CreateTask): Task = transaction {
        taskMapper.createTask(createTask)
            .let { taskMapper.getTask(it) ?: throw SystemException() }
    }

    override fun updateTask(taskId: Long, createTask: CreateTask): Task = transaction {
        taskValidationService.checkTaskExists(taskId)
        taskMapper.updateTask(taskId, createTask)
        taskMapper.getTask(taskId) ?: throw SystemException()
    }

    override fun deleteTask(taskId: Long) {
        taskValidationService.checkTaskExists(taskId)
        taskMapper.deleteTask(taskId)
    }
}
