package me.hubertus248.timer.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.*

object Config {

    private val config = HoconApplicationConfig(ConfigFactory.load())

    fun getProperty(key: String): String? = config.propertyOrNull(key)?.getString()

    fun requireProperty(key: String): String = config.property(key).getString()
}
