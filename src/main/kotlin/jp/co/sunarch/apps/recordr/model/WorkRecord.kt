package jp.co.sunarch.apps.recordr.model

class WorkRecord(
    val userId: UserId,
    val date: WorkDate,
    val startTime: WorkTime?,
    val endTime: WorkTime?,
    val restHours: WorkHours?,
    val notes: String?
) {

  val workHours: WorkHours?
    get() = startTime?.let { start ->
      endTime?.let { end ->
        restHours?.let { rest ->
          if (start > end || rest < WorkHours.ZERO) {
            null
          } else {
            val between = WorkHours.between(start, end)
            if (between < rest) {
              null
            } else {
              between - rest
            }
          }
        }
      }
    }

}