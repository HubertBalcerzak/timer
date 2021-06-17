package me.hubertus248.timer.koin

import me.hubertus248.timer.task.mapper.TaskMapper
import me.hubertus248.timer.task.service.TaskService
import me.hubertus248.timer.task.service.TaskServiceImpl
import me.hubertus248.timer.task.service.TaskValidationService
import me.hubertus248.timer.task.service.TaskValidationServiceImpl
import org.koin.dsl.module

val timerModule = module {
    single<TaskService> { TaskServiceImpl(get(), get()) }
    single { TaskMapper() }
    single<TaskValidationService> { TaskValidationServiceImpl(get()) }
}
