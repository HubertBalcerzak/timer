package me.hubertus248.timer.config

import com.viartemev.ktor.flyway.FlywayFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database

@OptIn(KtorExperimentalAPI::class)
fun Application.configureDatabase(){
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
}
