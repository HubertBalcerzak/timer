package me.hubertus248.timer

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import me.hubertus248.timer.config.configureCors
import me.hubertus248.timer.config.configureDatabase
import me.hubertus248.timer.config.configureExceptionHandler
import me.hubertus248.timer.koin.configureKoin
import me.hubertus248.timer.task.service.TaskService
import me.hubertus248.timer.task.taskRouting
import org.koin.ktor.ext.inject
import org.slf4j.event.Level
import java.util.logging.Logger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

    configureDatabase()
    configureExceptionHandler()
    configureCors()
    configureKoin()

    install(ContentNegotiation) {
        json()
    }
    install(CallLogging){
        level = Level.DEBUG
    }

    val taskService: TaskService by inject()

    routing {
        taskRouting(taskService)
    }
}
