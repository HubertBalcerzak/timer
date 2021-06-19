package me.hubertus248.timer.config

import com.viartemev.ktor.flyway.FlywayFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalAPI::class)
fun Application.configureDatabase() {
    val properties: DatasourceProperties by inject()

    val config = HikariConfig().apply {
        driverClassName = properties.driver
        jdbcUrl = properties.url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        password = properties.password
        username = properties.username
        validate()
    }
    val database = HikariDataSource(config)
    Database.connect(database)
    install(FlywayFeature) {
        dataSource = database
        schemas
    }
}
