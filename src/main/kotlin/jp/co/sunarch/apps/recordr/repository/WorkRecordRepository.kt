package jp.co.sunarch.apps.recordr.repository

import jp.co.sunarch.apps.recordr.model.UserId
import jp.co.sunarch.apps.recordr.model.WorkDate
import jp.co.sunarch.apps.recordr.model.WorkRecord

interface WorkRecordRepository {

  fun findByUserIdAndDate(userId: UserId, date: WorkDate): WorkRecord?

  fun findByUserIdAndYearMonth(userId: UserId,  year: Int, month: Int): List<WorkRecord>

  fun save(record: WorkRecord)

  fun deleteByUserIdAndDate(userId: UserId, date: WorkDate)

}