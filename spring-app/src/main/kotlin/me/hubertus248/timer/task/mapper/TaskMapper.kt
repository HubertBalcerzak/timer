package me.hubertus248.timer.task.mapper

import me.hubertus248.timer.common.pagination.Page
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.task.model.Task
import me.hubertus248.timer.task.mapper.tables.Tasks
import me.hubertus248.timer.task.model.CreateTask
import org.jetbrains.exposed.sql.*

class TaskMapper {

    fun getTasks(pageable: Pageable, query: String?, userId: Long): Page<Task> =
        (if (query == null) Tasks.selectAll()
        else Tasks.select { (Tasks.name like "%$query%") and (Tasks.userId eq userId) })
            .limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())
            .orderBy(Tasks.id)
            .map { Task(it[Tasks.id].value, it[Tasks.name]) }
            .let { Page.create(pageable, countTasks(userId), it) }


    fun createTask(createTask: CreateTask, userId: Long): Long {
        return Tasks.insertAndGetId {
            it[name] = createTask.name
            it[Tasks.userId] = userId
        }.value
    }

    fun getTask(taskId: Long, userId: Long): Task? {
        return Tasks.select {
            (Tasks.id eq taskId) and (Tasks.userId eq userId)
        }.firstOrNull()?.let {
            Task(
                it[Tasks.id].value,
                it[Tasks.name]
            )
        }
    }

    fun updateTask(taskId: Long, createTask: CreateTask, userId: Long) {
        Tasks.update({ (Tasks.id eq taskId) and (Tasks.userId eq userId) }) {
            it[name] = createTask.name
        }
    }

    fun deleteTask(taskId: Long, userId: Long) {
        Tasks.deleteWhere { (Tasks.id eq taskId) and (Tasks.userId eq userId) }
    }

    fun countTasks(userId: Long): Long = Tasks.select { Tasks.userId eq userId }.count()

    fun taskExists(taskId: Long, userId: Long) = Tasks
        .select { (Tasks.id eq taskId) and (Tasks.userId eq userId) }
        .count() > 0
}
