package me.hubertus248.timer

import com.viartemev.ktor.flyway.FlywayFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.*
import me.hubertus248.timer.common.exception.ApplicationException
import me.hubertus248.timer.koin.timerModule
import me.hubertus248.timer.task.service.TaskService
import me.hubertus248.timer.task.taskRouting
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.modules

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(KtorExperimentalAPI::class)
fun Application.module(testing: Boolean = false) {

    val config = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = "jdbc:postgresql://localhost/timer"
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        password = "password"
        username = "postgres"
        validate()
    }
    val database = HikariDataSource(config)
    Database.connect(database)
    install(FlywayFeature) {
        dataSource = database
        schemas
    }

    install(ContentNegotiation) {
        json()
    }

    install(Koin) {
        modules(timerModule)
    }

    install(StatusPages) {
        exception(ApplicationException::class.java) { exception ->
            call.respond(exception.statusCode, exception.errorMessage)
        }
        exception(Exception::class.java) {
            call.respond(HttpStatusCode.InternalServerError, "Internal server error")
        }
    }

    val taskService: TaskService by inject()

    routing {
        taskRouting(taskService)
    }
}
