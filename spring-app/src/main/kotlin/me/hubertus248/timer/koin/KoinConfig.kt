package me.hubertus248.timer.koin

import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(timerModule)
    }
}
