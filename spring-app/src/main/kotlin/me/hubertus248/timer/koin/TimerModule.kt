package me.hubertus248.timer.koin

import me.hubertus248.timer.config.DatasourceProperties
import me.hubertus248.timer.config.EnvironmentProperties
import me.hubertus248.timer.event.mapper.EventMapper
import me.hubertus248.timer.event.service.EventService
import me.hubertus248.timer.event.service.EventServiceImpl
import me.hubertus248.timer.task.mapper.TaskMapper
import me.hubertus248.timer.task.service.TaskService
import me.hubertus248.timer.task.service.TaskServiceImpl
import me.hubertus248.timer.task.service.TaskValidationService
import me.hubertus248.timer.task.service.TaskValidationServiceImpl
import me.hubertus248.timer.user.mapper.UserMapper
import me.hubertus248.timer.user.service.UserService
import me.hubertus248.timer.user.service.UserServiceImpl
import org.koin.dsl.module

val timerModule = module {
    single<TaskService> { TaskServiceImpl() }
    single { TaskMapper() }
    single<TaskValidationService> { TaskValidationServiceImpl(get()) }
    single { DatasourceProperties() }
    single { EnvironmentProperties() }
    single { UserMapper() }
    single<UserService> { UserServiceImpl() }
    single { EventMapper() }
    single<EventService> { EventServiceImpl() }
}
