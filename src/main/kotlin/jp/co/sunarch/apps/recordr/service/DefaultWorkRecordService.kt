package jp.co.sunarch.apps.recordr.service

import jp.co.sunarch.apps.recordr.model.UserId
import jp.co.sunarch.apps.recordr.model.WorkDate
import jp.co.sunarch.apps.recordr.model.WorkRecord
import jp.co.sunarch.apps.recordr.repository.WorkRecordRepository
import jp.co.sunarch.apps.recordr.service.WorkRecordService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultWorkRecordService(
    val workRecordRepository: WorkRecordRepository
) : WorkRecordService {

  @Transactional(readOnly = true)
  override fun findByYearMonth(userId: UserId, year: Int, month: Int): List<WorkRecord> {
    return workRecordRepository.findByUserIdAndYearMonth(userId, year, month)
  }

  @Transactional(readOnly = true)
  override fun findByDate(userId: UserId, date: WorkDate): WorkRecord? {
    return workRecordRepository.findByUserIdAndDate(userId, date)
  }

  @Transactional
  override fun save(record: WorkRecord) {
    workRecordRepository.save(record)
  }

  @Transactional
  override fun deleteByDate(userId: UserId, date: WorkDate) {
    workRecordRepository.deleteByUserIdAndDate(userId, date)
  }

}