package me.hubertus248.timer.event.service

import me.hubertus248.timer.event.mapper.EventMapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate


interface EventValidationService {

    fun checkNoEventOpen(date: LocalDate, userId: Long)
}

class EventValidationServiceImpl : EventValidationService, KoinComponent {

    val eventMapper by inject<EventMapper>()

    override fun checkNoEventOpen(date: LocalDate, userId: Long) {
        TODO("Not yet implemented")
    }
}