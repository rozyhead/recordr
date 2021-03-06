package jp.co.sunarch.apps.recordr.controller

import jp.co.sunarch.apps.recordr.model.*
import jp.co.sunarch.apps.recordr.service.WorkRecordService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class WorkRecordController(
    val workRecordService: WorkRecordService
) {

  data class SaveJson(
      val startTime: WorkTime?,
      val endTime: WorkTime?,
      val restHours: WorkHours?,
      val notes: String?
  ) {
    fun toRecord(userId: UserId, date: WorkDate) = WorkRecord(
        userId,
        date,
        startTime,
        endTime,
        restHours,
        notes
    )
  }

  @GetMapping("/api/v1/me/records")
  fun findByYearMonth(
      @RequestParam("year") year: Int,
      @RequestParam("month") month: Int,
      principal: Principal
  ): List<WorkRecord> {
    val userId = UserId(principal.name)
    return workRecordService.findByYearMonth(userId, year, month)
  }

  @GetMapping("/api/v1/me/records/{date}")
  fun findByDate(
      @PathVariable("date") date: String,
      principal: Principal
  ): WorkRecord? {
    val userId = UserId(principal.name)
    return workRecordService.findByDate(userId, WorkDate.parse(date))
        ?: throw ResourceNotFoundException("The record for date '$date' is not found")
  }

  @RequestMapping(
      path = arrayOf("/api/v1/me/records/{date}"),
      method = arrayOf(RequestMethod.POST, RequestMethod.PUT)
  )
  fun save(
      @PathVariable("date") date: String,
      @RequestBody json: SaveJson,
      principal: Principal
  ): WorkRecord {
    val userId = UserId(principal.name)
    val record = json.toRecord(userId, WorkDate.parse(date))
    workRecordService.save(record)
    return findByDate(date, principal)!!
  }

  @DeleteMapping("/api/v1/me/records/{date}")
  fun deleteByDate(
      @PathVariable("date") date: String,
      principal: Principal
  ) {
    val userId = UserId(principal.name)
    workRecordService.deleteByDate(userId, WorkDate.parse(date))
  }
}