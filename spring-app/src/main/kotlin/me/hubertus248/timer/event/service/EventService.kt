package me.hubertus248.timer.event.service

import me.hubertus248.timer.event.mapper.EventMapper
import me.hubertus248.timer.event.model.StartEvent
import me.hubertus248.timer.security.KeycloakPrincipal
import me.hubertus248.timer.task.service.TaskValidationService
import me.hubertus248.timer.user.service.UserService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate

interface EventService {

    fun getEvents(date: LocalDate, principal: KeycloakPrincipal)

    fun startEvent(startEvent: StartEvent, principal: KeycloakPrincipal): Long

    fun closeEvent(taskId: Long, principal: KeycloakPrincipal)

    fun closeOpenEvent(principal: KeycloakPrincipal, timestamp: Instant)
}

class EventServiceImpl : EventService, KoinComponent {

    private val eventMapper by inject<EventMapper>()
    private val eventValidationService by inject<EventValidationService>()
    private val taskValidationService by inject<TaskValidationService>()
    private val userService by inject<UserService>()

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getEvents(date: LocalDate, principal: KeycloakPrincipal) {

    }

    override fun startEvent(startEvent: StartEvent, principal: KeycloakPrincipal) = transaction {
        val userId = userService.getUserId(principal)
        taskValidationService.checkTaskExists(startEvent.taskId, userId)

        if (eventMapper.isEventOpen(userId)) {
            closeOpenEvent(principal, Instant.now())
        }
        eventMapper.startEvent(startEvent.day, Instant.now(), userId)
            .also { log.info("Event for task ${startEvent.taskId} started. ID: $it") }
    }

    override fun closeEvent(taskId: Long, principal: KeycloakPrincipal) {
        TODO("Not yet implemented")
    }

    override fun closeOpenEvent(principal: KeycloakPrincipal, timestamp: Instant) = transaction {
        eventMapper.endEvent(userService.getUserId(principal), timestamp)
        log.info("Event for user ${principal.preferredUsername} closed at $timestamp")
    }

}