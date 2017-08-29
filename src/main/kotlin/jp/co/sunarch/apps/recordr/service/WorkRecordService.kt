package jp.co.sunarch.apps.recordr.service

import jp.co.sunarch.apps.recordr.model.UserId
import jp.co.sunarch.apps.recordr.model.WorkDate
import jp.co.sunarch.apps.recordr.model.WorkRecord

interface WorkRecordService {

  fun findByYearMonth(userId: UserId, year: Int, month: Int): List<WorkRecord>

  fun findByDate(userId: UserId, date: WorkDate): WorkRecord?

  fun save(record: WorkRecord)

  fun deleteByDate(userId: UserId, date: WorkDate)

}