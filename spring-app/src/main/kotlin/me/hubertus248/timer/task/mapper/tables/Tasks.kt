package me.hubertus248.timer.task.mapper.tables

import me.hubertus248.timer.user.mapper.tables.Users
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Tasks : LongIdTable() {
    val name: Column<String> = varchar("name", 128)
    val userId: Column<Long> = long("userId").references(Users.id)
}
