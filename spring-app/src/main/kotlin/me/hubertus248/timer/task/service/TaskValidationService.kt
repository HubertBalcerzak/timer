package me.hubertus248.timer.task.service

import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.task.mapper.TaskMapper

interface TaskValidationService {

    fun checkTaskExists(taskId: Long, userId: Long)
}

class TaskValidationServiceImpl(private val taskMapper: TaskMapper) : TaskValidationService {

    override fun checkTaskExists(taskId: Long, userId: Long) {
        if (!taskMapper.taskExists(taskId, userId)) {
            throw NotFoundException()
        }
    }

}
