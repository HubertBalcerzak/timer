package me.hubertus248.timer.task.mapper.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Tasks : LongIdTable() {
    val name:Column<String> = varchar("name", 128)
}
