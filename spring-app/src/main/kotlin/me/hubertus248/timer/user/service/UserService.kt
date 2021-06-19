package me.hubertus248.timer.user.service

import me.hubertus248.timer.common.exception.UnauthorizedException
import me.hubertus248.timer.security.KeycloakPrincipal
import me.hubertus248.timer.user.mapper.UserMapper
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserService {

    fun createIfNotExists(principal: KeycloakPrincipal): Boolean

    fun getUserId(principal: KeycloakPrincipal): Long
}

class UserServiceImpl : UserService, KoinComponent {

    private val userMapper: UserMapper by inject()

    override fun createIfNotExists(principal: KeycloakPrincipal): Boolean = transaction {
        if (!userMapper.isUserExists(principal.subject)) {
            userMapper.createUser(principal.subject)
            return@transaction true
        }
        false
    }

    override fun getUserId(principal: KeycloakPrincipal): Long =
        userMapper.getUserId(principal.subject) ?: throw UnauthorizedException()

}
