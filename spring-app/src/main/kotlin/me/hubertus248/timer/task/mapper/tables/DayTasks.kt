package me.hubertus248.timer.task.mapper.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.timestamp

object DayTasks: LongIdTable(name = "day_tasks") {
    val taskId = long("taskid").references(Tasks.id)
    val day = date("day")
    val createdAt = timestamp("createdat")
}
