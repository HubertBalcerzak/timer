package me.hubertus248.timer.event.service

import io.ktor.util.*
import me.hubertus248.timer.common.exception.BadRequestException
import me.hubertus248.timer.common.exception.NotFoundException
import me.hubertus248.timer.event.mapper.EventMapper
import me.hubertus248.timer.event.model.Event
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.time.LocalDate


interface EventValidationService {

    fun checkEventExists(eventId: Long, userId: Long)

    fun checkEventStartTime(start: Instant, event: Event)

    fun checkEventEndTime(end: Instant, event: Event)
}

class EventValidationServiceImpl : EventValidationService, KoinComponent {

    private val eventMapper by inject<EventMapper>()

    override fun checkEventExists(eventId: Long, userId: Long) {
        if (!eventMapper.getEventExists(eventId, userId)) {
            throw NotFoundException()
        }
    }

    override fun checkEventStartTime(start: Instant, event: Event) {
        if (event.end?.isBefore(start) == true) {
            throw BadRequestException()
        }
    }

    override fun checkEventEndTime(end: Instant, event: Event) {
        if(event.start.isAfter(end)){
            throw BadRequestException()
        }
    }

}