package me.hubertus248.timer.event.mapper.tables

import me.hubertus248.timer.task.mapper.tables.Tasks
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Events : LongIdTable() {
    val taskId = long("taskid").references(Tasks.id)
    val day = date("day")
    val start = timestamp("starttimestamp")
    val end = timestamp("endtimestamp").nullable()
}