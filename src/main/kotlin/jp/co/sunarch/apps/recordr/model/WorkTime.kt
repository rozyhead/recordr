package jp.co.sunarch.apps.recordr.model

import java.time.Duration
import java.util.Comparator.*

data class WorkTime(val hour: Int, val minute: Int) : Comparable<WorkTime> {

  fun durationTo(other: WorkTime): Duration {
    require(this <= other, { "The other must be >= this" })

    return Duration.ofHours((other.hour - hour).toLong())
        .plusMinutes((other.minute - minute).toLong())
  }

  override fun toString(): String {
    return String.format("%02d:%02d", hour, minute)
  }

  override fun compareTo(other: WorkTime): Int = comparator.compare(this, other)

  companion object {

    fun of(hour: Int, minute: Int): WorkTime {
      require(hour >= 0, { "The hour must be >= 0" })
      require(minute >= 0, { "The minute must be >= 0" })
      return WorkTime(hour + minute / 60, minute % 60)
    }

    val comparator: Comparator<WorkTime> = comparing(WorkTime::hour)
        .thenComparing(comparing(WorkTime::minute))

  }

}