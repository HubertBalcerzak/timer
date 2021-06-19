package me.hubertus248.timer.user.mapper.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Users : LongIdTable() {
    val subject: Column<String> = varchar("subject", 128).uniqueIndex()
}
