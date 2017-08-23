package jp.co.sunarch.apps.recordr.repository.exposed

import org.jetbrains.exposed.sql.Table

object WorkRecordTable : Table("work_record") {
  val userId = varchar("user_id", 255).primaryKey(1)
  val date = varchar("date", 10).primaryKey(2)
  val startTime = varchar("start_time", 5).nullable()
  val endTime = varchar("end_time", 5).nullable()
  val restMinutes = integer("rest_minutes").nullable()
  val notes = text("notes").nullable()
}