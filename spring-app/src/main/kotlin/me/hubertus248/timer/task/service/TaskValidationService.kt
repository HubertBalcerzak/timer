package me.hubertus248.timer.task.service

import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.task.mapper.TaskMapper

interface TaskValidationService {

    fun checkTaskExists(taskId: Long)
}

class TaskValidationServiceImpl(private val taskMapper: TaskMapper) : TaskValidationService {

    override fun checkTaskExists(taskId: Long) {
        if (!taskMapper.taskExists(taskId)) {
            throw NotFoundException()
        }
    }

}
