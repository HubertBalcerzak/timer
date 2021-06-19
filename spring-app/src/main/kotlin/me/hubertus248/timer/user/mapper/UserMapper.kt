package me.hubertus248.timer.user.mapper

import me.hubertus248.timer.user.mapper.tables.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserMapper {

    fun isUserExists(subject: String): Boolean {
        return Users.select { Users.subject eq subject }.count() > 0
    }

    fun createUser(subject: String) {
        Users.insert {
            it[Users.subject] = subject
        }
    }

    fun getUserId(subject: String): Long? = Users.select { Users.subject eq subject }
        .map { it[Users.id].value }.firstOrNull()
}
