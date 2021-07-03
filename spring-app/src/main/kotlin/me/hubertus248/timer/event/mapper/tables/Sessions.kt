package me.hubertus248.timer.event.mapper.tables

import me.hubertus248.timer.user.mapper.tables.Users
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Sessions : LongIdTable() {
    val userId = long("userid").references(Users.id)
    val createdAt = timestamp("createdat")
    val day = date("day")
}