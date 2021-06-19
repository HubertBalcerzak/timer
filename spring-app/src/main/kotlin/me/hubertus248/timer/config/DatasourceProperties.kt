package me.hubertus248.timer.config

class DatasourceProperties(
    val driver: String = Config.requireProperty("datasource.driver"),
    val url: String = Config.requireProperty("datasource.url"),
    val username: String = Config.requireProperty("datasource.username"),
    val password: String = Config.requireProperty("datasource.password")
)
