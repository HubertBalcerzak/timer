package me.hubertus248.timer.task.mapper

import me.hubertus248.timer.common.pagination.Page
import me.hubertus248.timer.common.pagination.Pageable
import me.hubertus248.timer.task.model.Task
import me.hubertus248.timer.task.mapper.tables.Tasks
import me.hubertus248.timer.task.model.CreateTask
import org.jetbrains.exposed.sql.*

class TaskMapper {

    fun getTasks(pageable: Pageable, query: String?): Page<Task> =
        (if (query == null) Tasks.selectAll()
        else Tasks.select { Tasks.name like "%$query%" })
            .limit(pageable.pageSize, offset = ((pageable.page - 1) * pageable.pageSize).toLong())
            .orderBy(Tasks.id)
            .map { Task(it[Tasks.id].value, it[Tasks.name]) }
            .let { Page.create(pageable, countTasks(), it) }


    fun createTask(createTask: CreateTask): Long {
        return Tasks.insertAndGetId {
            it[name] = createTask.name
        }.value
    }

    fun getTask(taskId: Long): Task? {
        return Tasks.select { Tasks.id eq taskId }.firstOrNull()?.let {
            Task(
                it[Tasks.id].value,
                it[Tasks.name]
            )
        }
    }

    fun updateTask(taskId: Long, createTask: CreateTask) {
        Tasks.update({ Tasks.id eq taskId }) {
            it[Tasks.name] = createTask.name
        }
    }

    fun deleteTask(taskId: Long) {
        Tasks.deleteWhere { Tasks.id eq taskId }
    }

    fun countTasks(): Long = Tasks.selectAll().count()

    fun taskExists(taskId: Long) = Tasks.select { Tasks.id eq taskId }.count() > 0


}
