package me.hubertus248.timer.task.service

import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.task.mapper.TaskMapper
import java.time.LocalDate

interface TaskValidationService {

    fun checkTaskExists(taskId: Long, userId: Long)

    fun checkTaskNotAdded(taskId: Long, date: LocalDate)
}

class TaskValidationServiceImpl(private val taskMapper: TaskMapper) : TaskValidationService {

    override fun checkTaskExists(taskId: Long, userId: Long) {
        if (!taskMapper.taskExists(taskId, userId)) {
            throw NotFoundException()
        }
    }

    override fun checkTaskNotAdded(taskId: Long, date: LocalDate) {
        if (taskMapper.dayTaskExists(taskId, date)) {
            throw BadRequestException()
        }
    }

}
