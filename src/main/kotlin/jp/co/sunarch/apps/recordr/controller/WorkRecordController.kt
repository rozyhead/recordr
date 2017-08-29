package jp.co.sunarch.apps.recordr.controller

import jp.co.sunarch.apps.recordr.model.*
import jp.co.sunarch.apps.recordr.service.WorkRecordService
import org.springframework.http.HttpStatus
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
  }

  @PostMapping("/api/v1/me/records/{date}")
  @PutMapping("/api/v1/me/records/{date}")
  fun save(
      @PathVariable("date") date: WorkDate,
      @RequestBody json: SaveJson,
      principal: Principal
  ) {
    val userId = UserId(principal.name)
    val record = json.toRecord(userId, date)
    workRecordService.save(record)
  }

  @DeleteMapping("/api/v1/me/records/{date}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun deleteByDate(
      @PathVariable("date") date: WorkDate,
      principal: Principal
  ) {
    val userId = UserId(principal.name)
    workRecordService.deleteByDate(userId, date)
  }
}