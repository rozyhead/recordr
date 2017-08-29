package jp.co.sunarch.apps.recordr.repository.exposed

import jp.co.sunarch.apps.recordr.model.*
import jp.co.sunarch.apps.recordr.repository.WorkRecordRepository
import org.jetbrains.exposed.sql.*
import jp.co.sunarch.apps.recordr.repository.exposed.WorkRecordTable as T

class ExposedWorkRecordRepository : WorkRecordRepository {

  override fun findByUserIdAndDate(userId: UserId, date: WorkDate): WorkRecord? {
    return T.select {
      T.userId.eq(userId.value) and
          T.date.eq(date.toString())
    }.singleOrNull()?.let(::mapToWorkRecord)
  }

  override fun findByUserIdAndYearMonth(userId: UserId, year: Int, month: Int): List<WorkRecord> {
    return T.select {
      T.userId.eq(userId.value) and
          T.date.like(String.format("%04d-%02d", year, month) + "%")
    }.orderBy(T.date).toList().map(::mapToWorkRecord)
  }

  override fun save(record: WorkRecord) {
    if (findByUserIdAndDate(record.userId, record.date) == null) {
      T.insert {
        it[T.userId] = record.userId.value
        it[T.date] = record.date.toString()
        it[T.startTime] = record.startTime?.toString()
        it[T.endTime] = record.endTime?.toString()
        it[T.restMinutes] = record.restHours?.minutes
        it[T.notes] = record.notes
      }
    } else {
      T.update({
        T.userId.eq(record.userId.value) and
            T.date.eq(record.date.toString())
      }) {
        it[T.startTime] = record.startTime?.toString()
        it[T.endTime] = record.endTime?.toString()
        it[T.restMinutes] = record.restHours?.minutes
        it[T.notes] = record.notes
      }
    }
  }

  override fun deleteByUserIdAndDate(userId: UserId, date: WorkDate) {
    T.deleteWhere {
      T.userId.eq(userId.value) and T.date.eq(date.toString())
    }
  }

}

private fun mapToWorkRecord(row: ResultRow): WorkRecord = WorkRecord(
    userId = row[T.userId].let { UserId(it) },
    date = row[T.date].let { WorkDate.parse(it) },
    startTime = row[T.startTime]?.let { WorkTime.parse(it) },
    endTime = row[T.endTime]?.let { WorkTime.parse(it) },
    restHours = row[T.restMinutes]?.let { WorkHours(it) },
    notes = row[T.notes]
)
