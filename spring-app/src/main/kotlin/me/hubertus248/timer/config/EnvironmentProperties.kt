package me.hubertus248.timer.config

class EnvironmentProperties(
    val cors: Boolean = Config.requireProperty("timer.cors").toBooleanStrict()
)
